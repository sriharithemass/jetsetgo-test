package com.training.repositories;

import com.training.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFlightName(String flightName);
    boolean existsByFlightNumber(String flightNumber);
}
