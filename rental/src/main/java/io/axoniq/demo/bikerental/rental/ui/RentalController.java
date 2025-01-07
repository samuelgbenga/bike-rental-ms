package io.axoniq.demo.bikerental.rental.ui;

import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import io.axoniq.demo.bikerental.coreapi.rental.BikeStatusList;
import io.axoniq.demo.bikerental.coreapi.rental.BikeStatusNamedQueries;
import io.axoniq.demo.bikerental.coreapi.rental.RegisterBikeCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/bikes")
    public CompletableFuture<BikeStatusList> findAll() {
        return queryGateway.query(
                BikeStatusNamedQueries.FIND_ALL,
                null,
                ResponseTypes.instanceOf(BikeStatusList.class)
        ).exceptionally(ex -> {
            // Log or handle the error
            throw new RuntimeException("Failed to retrieve bikes", ex);
        });

//        return queryGateway.query(
//                BikeStatusNamedQueries.FIND_ALL,
//                null,
//                ResponseTypes.multipleInstancesOf(BikeStatus.class)
//        ).thenApply(response -> {
//            System.out.println("Received type from query gateway: " + response.getClass());
//            if (!response.isEmpty()) {
//                System.out.println("First element type: " + response.get(0).getClass());
//            }
//            return response;
//        }).exceptionally(ex -> {
//            System.err.println("Error retrieving bikes: " + ex.getMessage());
//            throw new RuntimeException("Failed to retrieve bikes", ex);
//        });
    }

    @GetMapping("/bikes/{bikeId}")
    public CompletableFuture<BikeStatus> findStatus(@PathVariable("bikeId") String bikeId) {
        return queryGateway.query(BikeStatusNamedQueries.FIND_ONE, bikeId, BikeStatus.class);
    }


}
