package io.axoniq.demo.bikerental.rental.ui;

import io.axoniq.demo.bikerental.coreapi.rental.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
public class RentalController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;


    public RentalController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;

    }

    @PostMapping("/bikes")
    public CompletableFuture<String> registerBike(
            @RequestParam("bikeType") String bikeType,
            @RequestParam("location") String location) {

        RegisterBikeCommand registerBikeCommand =
                new RegisterBikeCommand(
                        UUID.randomUUID().toString(),
                        bikeType,
                        location);

        CompletableFuture<String> commandResult =
                commandGateway.send(registerBikeCommand);

        return commandResult;
    }

    @PostMapping("/requestBike")
    public CompletableFuture<String> requestBike(@RequestParam("bikeId") String bikeId, @RequestParam(value = "renter", required = false) String renter) {
        CompletableFuture<String> commandResult =
                commandGateway.send(new RequestBikeCommand(bikeId, renter != null
                        ? renter : null));
        return commandResult;
    }

    @PostMapping("/returnBike")
    public CompletableFuture<String> returnBike(@RequestParam("bikeId") String bikeId, @RequestParam("location") String location) {
        return commandGateway.send(new ReturnBikeCommand(bikeId, location));
    }

//    @PostMapping("acceptPayment")
//    public CompletableFuture<Void> acceptPayment(@RequestParam("id") String paymentId) {
//        return commandGateway.send(new ConfirmPaymentCommand(paymentId));
//    }

//    @PostMapping(value = "/generateRentals")
//    public Flux<String> generateData(@RequestParam(value = "bikeType") String bikeType,
//                                     @RequestParam("loops") int loops,
//                                     @RequestParam(value = "concurrency", defaultValue = "1") int concurrency,
//                                     @RequestParam(value = "abandonPaymentFactor", defaultValue = "100") int abandonPaymentFactor,
//                                     @RequestParam(value = "delay", defaultValue = "0")int delay) {
//
//        return this.bikeRentalDataGenerator.generateRentals(bikeType, loops, concurrency, abandonPaymentFactor, delay);
//    }

    //tag::generateBikes[]
    // This is for testing purpose
//    @PostMapping("/bikes/batch") // <.>
//    public CompletableFuture<Void> generateBikes(@RequestParam("count") int bikeCount,              //<.>
//                                                 @RequestParam(value = "type") String bikeType) {   //<.>
//        CompletableFuture<Void> all = CompletableFuture.completedFuture(null);
//        for (int i = 0; i < bikeCount; i++) {
//            all = CompletableFuture.allOf(all,
//                    commandGateway.send(new RegisterBikeCommand(UUID.randomUUID().toString(), bikeType, this.bikeRentalDataGenerator.randomLocation())));
//        }
//        return all;
//    }
    //end::generateBikes[]

    @GetMapping("/bikes")
    public CompletableFuture<List<BikeStatus>> findAll() {
        return queryGateway.query(
                BikeStatusNamedQueries.FIND_ALL,
                null,
                ResponseTypes.multipleInstancesOf(BikeStatus.class)
        ).exceptionally(ex -> {
            // Log or handle the error
            throw new RuntimeException("Failed to retrieve bikes", ex);
        });

    }

    @GetMapping("/bikes/{bikeId}")
    public CompletableFuture<BikeStatus> findStatus(@PathVariable("bikeId") String bikeId) {
        return queryGateway.query(BikeStatusNamedQueries.FIND_ONE, bikeId, BikeStatus.class);
    }



}
