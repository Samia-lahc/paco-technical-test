package technical.test.renderer.facades;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.renderer.services.FlightService;
import technical.test.renderer.viewmodels.FlightViewModel;

@Component
public class FlightFacade {

    private final FlightService flightService;

    public FlightFacade(FlightService flightService) {
        this.flightService = flightService;
    }

    public Flux<FlightViewModel> getFlights(String sortBy, int page) {
        return this.flightService.getFlights(sortBy, page);
    }

    public Mono<FlightViewModel> createFlight(FlightViewModel flight) {
        return this.flightService.createFlight(flight);
    }
}
