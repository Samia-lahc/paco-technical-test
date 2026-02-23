package technical.test.renderer.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import technical.test.renderer.facades.FlightFacade;
import technical.test.renderer.viewmodels.FlightViewModel;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TechnicalController {

    @Autowired
    private FlightFacade flightFacade;

    @GetMapping
    public Mono<String> getMarketPlaceReturnCouponPage(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "price") String sortBy,
                                                       final Model model) {
        model.addAttribute("flights", this.flightFacade.getFlights(sortBy, page));
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSort", sortBy);

        return Mono.just("pages/index");
    }
    @GetMapping("/admin/flight")
    public String getAdminFlightPage(Model model) {
        model.addAttribute("flight", new FlightViewModel());
        return "pages/admin-flight";
    }

    @PostMapping("/admin/flight")
    public Mono<String> createFlight(@ModelAttribute FlightViewModel flight, Model model) {
        return this.flightFacade.createFlight(flight)
                .thenReturn("redirect:/");
    }
}
