package com.training.controllers;

import com.training.models.Flight;
import com.training.services.impl.FlightServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class FlightController {

    @Autowired
    private FlightServiceImpl flightService;

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize
    ){
        List<Flight> flights = flightService.getAllFlights(pageNumber, pageSize);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/flights/{flightId}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long flightId){
        Flight flight = flightService.getFlightById(flightId);
        return new ResponseEntity<>(flight,HttpStatus.OK);
    }

    @PostMapping("/flights")
    public ResponseEntity<String> addFlight(@RequestBody Flight flight){
        String message = flightService.createFlight(flight);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PutMapping("/flights/{flightId}")
    public ResponseEntity<String> updateFlight(@RequestBody Flight flight, @PathVariable Long flightId){
        String message = flightService.updateFlight(flight,flightId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @DeleteMapping("/flights/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long flightId){
        String message = flightService.deleteFlight(flightId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}