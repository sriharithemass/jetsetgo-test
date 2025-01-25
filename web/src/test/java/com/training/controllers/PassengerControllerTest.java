package com.training.controllers;

import com.training.models.Passenger;
import com.training.services.impl.PassengerServiceImpl;
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

class PassengerControllerTest {

    @InjectMocks
    private PassengerController passengerController;

    @Mock
    private PassengerServiceImpl passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPassengersByUser() {
        Passenger passenger = new Passenger();
        List<Passenger> passengers = Collections.singletonList(passenger);
        String username = "testUser";

        when(passengerService.getPassengersByUser(username, 0, 50)).thenReturn(passengers);

        ResponseEntity<List<Passenger>> response = passengerController.getPassengersByUser(username, 0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passengers, response.getBody());
    }

    @Test
    void testGetPassengerById() {
        Long passengerId = 1L;
        Passenger passenger = new Passenger();

        when(passengerService.getPassengerById(passengerId)).thenReturn(passenger);

        ResponseEntity<Passenger> response = passengerController.getPassengerById(passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(passenger, response.getBody());
    }

    @Test
    void testCreatePassenger() {
        Passenger passenger = new Passenger();
        String username = "testUser";
        String message = "Passenger added";

        when(passengerService.createPassenger(passenger, username)).thenReturn(message);

        ResponseEntity<String> response = passengerController.createPassenger(passenger, username);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testUpdatePassenger() {
        Long passengerId = 1L;
        Passenger passenger = new Passenger();
        String username = "testUser";
        String message = "Passenger updated";

        when(passengerService.updatePassenger(passenger, passengerId, username)).thenReturn(message);

        ResponseEntity<String> response = passengerController.updatePassenger(passenger, passengerId, username);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testDeletePassenger() {
        Long passengerId = 1L;
        String message = "Passenger deleted";

        when(passengerService.deletePassenger(passengerId)).thenReturn(message);

        ResponseEntity<String> response = passengerController.deletePassenger(passengerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}