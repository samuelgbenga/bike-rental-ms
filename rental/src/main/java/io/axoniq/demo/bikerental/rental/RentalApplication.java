package io.axoniq.demo.bikerental.rental;

import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.modelling.saga.repository.jpa.SagaEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * Hello world!
 *
 */
@EntityScan(basePackageClasses = {BikeStatus.class, SagaEntry.class, TokenEntry.class})
@SpringBootApplication
public class RentalApplication
{
    public static void main(String[] args) {
        SpringApplication.run(RentalApplication.class, args);
    }
}
