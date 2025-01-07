package io.axoniq.demo.bikerental.rental.query;

import io.axoniq.demo.bikerental.coreapi.rental.BikeStatus;
import io.axoniq.demo.bikerental.coreapi.rental.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BikeStatusRepository extends JpaRepository<BikeStatus, String> {

    @Override
    List<BikeStatus> findAll();

    List<BikeStatus> findAllByBikeTypeAndStatus(String bikeType, RentalStatus rentalStatus);
    long countBikeStatusesByBikeType(String bikeType);
}
