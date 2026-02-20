package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

@Component
@RequiredArgsConstructor
public class FlightFacade {

    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;

    public Flux<FlightRepresentation> getAllFlights(Pageable pageable) {
        return flightService.getAllFlights(pageable)
                .flatMap(this::enrichWithAirports);
    }

    /**
     * @param flight
     * @return Mono<FlightRepresentation>
     *  Creation d'un vol
     */
    public Mono<FlightRepresentation> createFlight(FlightRepresentation flight) {
        FlightRecord record = flightMapper.convert(flight);
        return flightService.saveFlight(record)
                .flatMap(this::enrichWithAirports);
    }

    /**
     * Enrichit un FlightRecord avec ses aéroports d'origine et de destination,
     * puis le mappe en FlightRepresentation complète.
     */
    private Mono<FlightRepresentation> enrichWithAirports(FlightRecord flightRecord) {
        return Mono.zip(
                        airportService.findByIataCode(flightRecord.getOrigin()),
                        airportService.findByIataCode(flightRecord.getDestination())
                )
                .map(tuple -> buildRepresentation(flightRecord, tuple));
    }

    /**
     * Construit la FlightRepresentation à partir du record de vol et du tuple (origin, destination).
     */
    private FlightRepresentation buildRepresentation(FlightRecord flightRecord, Tuple2<AirportRecord, AirportRecord> airports) {
        AirportRecord origin = airports.getT1();
        AirportRecord destination = airports.getT2();

        FlightRepresentation representation = flightMapper.convert(flightRecord);
        representation.setOrigin(airportMapper.convert(origin));
        representation.setDestination(airportMapper.convert(destination));

        return representation;
    }
}