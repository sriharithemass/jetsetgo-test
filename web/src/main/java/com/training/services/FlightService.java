package com.training.services;

import com.training.models.Flight;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FlightService {
    List<Flight> getAllFlights(Integer pageNumber, Integer pageSize);
    Flight getFlightById(Long flightId);
    String createFlight(Flight flight);
    String updateFlight(Flight flight, Long flightId);
    String deleteFlight(Long flightId);
}
