package com.training.controllers;

import com.training.models.Flight;
import com.training.services.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FlightControllerTest {

    @InjectMocks
    private FlightController flightController;

    @Mock
    private FlightServiceImpl flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFlights() {
        Flight flight = new Flight();
        List<Flight> flights = Collections.singletonList(flight);

        when(flightService.getAllFlights(0, 50)).thenReturn(flights);

        ResponseEntity<List<Flight>> response = flightController.getAllFlights(0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flights, response.getBody());
    }

    @Test
    void testGetFlightById() {
        Long flightId = 1L;
        Flight flight = new Flight();

        when(flightService.getFlightById(flightId)).thenReturn(flight);

        ResponseEntity<Flight> response = flightController.getFlightById(flightId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flight, response.getBody());
    }

    @Test
    void testAddFlight() {
        Flight flight = new Flight();
        String message = "Flight added";

        when(flightService.createFlight(flight)).thenReturn(message);

        ResponseEntity<String> response = flightController.addFlight(flight);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testUpdateFlight() {
        Long flightId = 1L;
        Flight flight = new Flight();
        String message = "Flight updated";

        when(flightService.updateFlight(flight, flightId)).thenReturn(message);

        ResponseEntity<String> response = flightController.updateFlight(flight, flightId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testDeleteFlight() {
        Long flightId = 1L;
        String message = "Flight deleted";

        when(flightService.deleteFlight(flightId)).thenReturn(message);

        ResponseEntity<String> response = flightController.deleteFlight(flightId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}