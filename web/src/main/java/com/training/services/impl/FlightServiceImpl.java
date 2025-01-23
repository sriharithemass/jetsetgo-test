package com.training.services.impl;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Flight;
import com.training.repositories.FlightRepository;
import com.training.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Override
    public List<Flight> getAllFlights(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);

        List<Flight> flights = flightRepository.findAll(pageDetails).getContent();
        if (flights.isEmpty())
            throw new APIException("Flight list is empty");

        return flights;
    }

    @Override
    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(()-> new APIException(AppConstants.FLIGHT_NOT_FOUND));
    }

    @Override
    public String createFlight(Flight flight) {
        if (flightRepository.existsByFlightName(flight.getFlightName()))
            throw new APIException("flight name already exists");

        if (flightRepository.existsByFlightNumber(flight.getFlightNumber()))
            throw new APIException("flight number already exists");

        if (flight.getDepartureLocation().equalsIgnoreCase(flight.getArrivalLocation()))
            throw new APIException("location should not be same");

        flightRepository.save(flight);
        return "Flight Added";
    }

    @Override
    public String updateFlight(Flight flight, Long flightId) {
        if (!flightRepository.existsById(flightId))
            throw new APIException(AppConstants.FLIGHT_NOT_FOUND);

        if (flightRepository.existsByFlightName(flight.getFlightName())
                && !flightRepository.findById(flightId).get().getFlightName().equalsIgnoreCase(flight.getFlightName())){
            throw new APIException("flight name already exists");
        }

        if (flightRepository.existsByFlightNumber(flight.getFlightNumber())
                && !flightRepository.findById(flightId).get().getFlightNumber().equalsIgnoreCase(flight.getFlightNumber())){
            throw new APIException("flight number already exists");
        }

        flight.setFlightId(flightId);
        flightRepository.save(flight);

        return "Flight Updated";
    }

    @Override
    public String deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId))
            throw new APIException(AppConstants.FLIGHT_NOT_FOUND);

        flightRepository.deleteById(flightId);

        return "Flight Deleted";
    }
}
