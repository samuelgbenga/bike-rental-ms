package io.axoniq.demo.bikerental.coreapi.rental;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BikeStatus {

    @Id
    private String bikeId;
    private String bikeType;
    private String location;
    private String renter;
    private RentalStatus status;


    public BikeStatus() {
    }

    public BikeStatus(  String bikeId, String bikeType, String location) {
        this.bikeId = bikeId;
        this.bikeType = bikeType;
        this.location = location;

    }

    // Accessor methods
    public String getBikeId() {
        return bikeId;
    }

    public String getBikeType() {
        return bikeType;
    }

    public String getLocation() {
        return location;
    }

    public String getRenter() {
        return renter;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public String description() {
        switch (status) {
            case RENTED:
                return String.format("Bike %s was rented by %s in %s", bikeId, renter, location);
            case AVAILABLE:
                return String.format("Bike %s is available for rental in %s.", bikeId, location);
            case REQUESTED:
                return String.format("Bike %s is requested by %s in %s", bikeId, renter, location);
            default:
                return "Status unknown";
        }
    }
}
