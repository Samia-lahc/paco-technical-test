package technical.test.api.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.representation.FlightRepresentation;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightEndpoint {
    private final FlightFacade flightFacade;

    /**
     * @param page
     * @param sortBy
     * @return Flux<FlightRepresentation>
     *     Affichier la liste des vols avec le tri et la pagination
     */
    @GetMapping
    public Flux<FlightRepresentation> getAllFlights(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "origin") String sortBy) {
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sortBy).ascending());
        return flightFacade.getAllFlights(pageable);
    }

    /**
     * @param flight
     * @return Mono<FlightRepresentation>
     */
    @PostMapping
    public Mono<FlightRepresentation> createFlight(@RequestBody FlightRepresentation flight) {
        return flightFacade.createFlight(flight);
    }
}
