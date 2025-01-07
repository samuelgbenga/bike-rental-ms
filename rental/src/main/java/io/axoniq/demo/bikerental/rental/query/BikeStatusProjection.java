package io.axoniq.demo.bikerental.rental.query;


import io.axoniq.demo.bikerental.coreapi.rental.*;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BikeStatusProjection {

    private final BikeStatusRepository bikeStatusRepository;

    private final JdbcTemplate jdbcTemplate;

    public BikeStatusProjection(BikeStatusRepository repository, JdbcTemplate jdbcTemplate) {
        this.bikeStatusRepository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventHandler
    public void on(BikeRegisteredEvent event) {
        var bikeStatus = new BikeStatus(event.bikeId(), event.bikeType(), event.location());
        bikeStatusRepository.save(bikeStatus);
    }

    @QueryHandler(queryName = BikeStatusNamedQueries.FIND_ALL)
    public BikeStatusList findAll() {
  //      return bikeStatusRepository.findAll();

//        return new ArrayList<>(bikeStatusRepository.findAll());

//        List<BikeStatus> result = bikeStatusRepository.findAll();
//        System.out.println("Returned type from QueryHandler: " + result.getClass());
//        if (!result.isEmpty()) {
//            System.out.println("First element type: " + result.get(0).getClass());
//            System.out.println();
//            System.out.println(result);
//        }
//        return result;

        return new BikeStatusList(jdbcTemplate.query(
                "SELECT * FROM bike_status",
                (rs, rowNum) -> new BikeStatus(
                        rs.getString("bike_id"),
                        rs.getString("bike_type"),
                        rs.getString("location")
                )
        ));
    }

    @QueryHandler(queryName = BikeStatusNamedQueries.FIND_AVAILABLE)
    public List<BikeStatus> findAvailable(String bikeType) {
        return bikeStatusRepository.findAllByBikeTypeAndStatus(bikeType, RentalStatus.AVAILABLE);
    }

    @QueryHandler(queryName = BikeStatusNamedQueries.FIND_ONE)
    public BikeStatus findOne(String bikeId) {
        return bikeStatusRepository.findById(bikeId).orElse(null);

    }

}
