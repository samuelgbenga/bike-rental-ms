package io.axoniq.demo.bikerental.rental.query;


import io.axoniq.demo.bikerental.coreapi.rental.*;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BikeStatusProjection {
    private final BikeStatusRepository bikeStatusRepository;
    private final JdbcTemplate jdbcTemplate;
    //tag::UpdateEmitter[]
    private final QueryUpdateEmitter updateEmitter;

    public BikeStatusProjection(BikeStatusRepository repository, JdbcTemplate jdbcTemplate, QueryUpdateEmitter updateEmitter) {
        this.bikeStatusRepository = repository;
        this.jdbcTemplate = jdbcTemplate;
        this.updateEmitter = updateEmitter;
    }

    @EventHandler
    public void on(BikeRegisteredEvent event) {
        var bikeStatus = new BikeStatus(event.bikeId(), event.bikeType(), event.location());
        bikeStatusRepository.save(bikeStatus);
        updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bikeStatus); //<.>
        //end::UpdateEmitter[]
    }

    //end::BikeRegisteredEventHandler[]
    @EventHandler
    public void on(BikeRequestedEvent event) {
        bikeStatusRepository.findById(event.bikeId())
                .map(bs -> {
                    bs.requestedBy(event.renter());
                    return bs;
                })
                .ifPresent(bs -> {
                    updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bs);
                    updateEmitter.emit(String.class, event.bikeId()::equals, bs);
                });
    }

    @EventHandler
    public void on(BikeInUseEvent event) {
        bikeStatusRepository.findById(event.bikeId())
                .map(bs -> {
                    bs.rentedBy(event.renter());
                    return bs;
                })
                .ifPresent(bs -> {
                    updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bs);
                    updateEmitter.emit(String.class, event.bikeId()::equals, bs);
                });
    }

    @EventHandler
    public void on(BikeReturnedEvent event) {
        bikeStatusRepository.findById(event.bikeId())
                .map(bs -> {
                    bs.returnedAt(event.location());
                    return bs;
                })
                .ifPresent(bs -> {
                    updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bs);
                    updateEmitter.emit(String.class, event.bikeId()::equals, bs);
                });
    }

    @EventHandler
    public void on(RequestRejectedEvent event) {
        bikeStatusRepository.findById(event.bikeId())
                .map(bs -> {
                    bs.returnedAt(bs.getLocation());
                    return bs;
                })
                .ifPresent(bs -> {
                    updateEmitter.emit(q -> "findAll".equals(q.getQueryName()), bs);
                    updateEmitter.emit(String.class, event.bikeId()::equals, bs);
                });
    }

    //end::EventHandlers[]



//    @QueryHandler(queryName = BikeStatusNamedQueries.FIND_ALL)
//    public BikeStatusList findAll() {
//        return new BikeStatusList(jdbcTemplate.query(
//                "SELECT * FROM bike_status",
//                (rs, rowNum) -> new BikeStatus(
//                        rs.getString("bike_id"),
//                        rs.getString("bike_type"),
//                        rs.getString("location")
//                )
//        ));
//    }

    //tag::findAllQueryHandler[]
    @QueryHandler(queryName = BikeStatusNamedQueries.FIND_ALL) //<.>
    public Iterable<BikeStatus> findAll() { // <.>
        return bikeStatusRepository.findAll(); //<.>
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
