package com.training.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Flight;
import com.training.repositories.FlightRepository;
import com.training.services.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FlightServiceImplTest {

    @InjectMocks
    private FlightServiceImpl flightService;

    @Mock
    private FlightRepository flightRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFlights() {
        Flight flight = new Flight();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> page = new PageImpl<>(Collections.singletonList(flight));

        when(flightRepository.findAll(pageable)).thenReturn(page);

        List<Flight> result = flightService.getAllFlights(0, 10);

        assertEquals(1, result.size());
        assertEquals(flight, result.get(0));
    }

    @Test
    public void testGetAllFlights_EmptyList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Flight> page = new PageImpl<>(Collections.emptyList());

        when(flightRepository.findAll(pageable)).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.getAllFlights(0, 10);
        });

        assertEquals("Flight list is empty", exception.getMessage());
    }

    @Test
    public void testGetFlightById() {
        Flight flight = new Flight();
        flight.setFlightId(1L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Flight result = flightService.getFlightById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getFlightId());
    }

    @Test
    public void testGetFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.getFlightById(1L);
        });

        assertEquals(AppConstants.FLIGHT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testCreateFlight() {
        Flight flight = new Flight();
        flight.setFlightName("Test Flight");
        flight.setFlightNumber("123");
        flight.setDepartureLocation("NYC");
        flight.setArrivalLocation("LAX");

        when(flightRepository.existsByFlightName("Test Flight")).thenReturn(false);
        when(flightRepository.existsByFlightNumber("123")).thenReturn(false);
        when(flightRepository.save(flight)).thenReturn(flight);

        String result = flightService.createFlight(flight);

        assertEquals("Flight Added", result);
    }

    @Test
    public void testCreateFlight_NameExists() {
        Flight flight = new Flight();
        flight.setFlightName("Test Flight");

        when(flightRepository.existsByFlightName("Test Flight")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.createFlight(flight);
        });

        assertEquals("flight name already exists", exception.getMessage());
    }

    @Test
    public void testCreateFlight_NumberExists() {
        Flight flight = new Flight();
        flight.setFlightNumber("123");

        when(flightRepository.existsByFlightNumber("123")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.createFlight(flight);
        });

        assertEquals("flight number already exists", exception.getMessage());
    }

    @Test
    public void testCreateFlight_SameLocation() {
        Flight flight = new Flight();
        flight.setDepartureLocation("NYC");
        flight.setArrivalLocation("NYC");

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.createFlight(flight);
        });

        assertEquals("location should not be same", exception.getMessage());
    }

    @Test
    public void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setFlightId(1L);
        flight.setFlightName("Updated Flight");
        flight.setFlightNumber("123");

        Flight existingFlight = new Flight();
        existingFlight.setFlightId(1L);
        existingFlight.setFlightName("Old Flight");
        existingFlight.setFlightNumber("123");

        when(flightRepository.existsById(1L)).thenReturn(true);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(flight)).thenReturn(flight);

        String result = flightService.updateFlight(flight, 1L);

        assertEquals("Flight Updated", result);
    }

    @Test
    public void testUpdateFlight_NotFound() {
        Flight flight = new Flight();
        flight.setFlightId(1L);

        when(flightRepository.existsById(1L)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.updateFlight(flight, 1L);
        });

        assertEquals(AppConstants.FLIGHT_NOT_FOUND, exception.getMessage());
    }

//    @Test
//    public void testUpdateFlight_NameExists() {
//        Flight flight = new Flight();
//        flight.setFlightId(1L);
//        flight.setFlightName("Updated Flight");
//
//        Flight existingFlight = new Flight();
//        existingFlight.setFlightId(2L);
//        existingFlight.setFlightName("Updated Flight");
//
//        when(flightRepository.existsById(1L)).thenReturn(true);
//        when(flightRepository.existsByFlightName("Updated Flight")).thenReturn(true);
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
//
//        APIException exception = assertThrows(APIException.class, () -> {
//            flightService.updateFlight(flight, 1L);
//        });
//
//        assertEquals("flight name already exists", exception.getMessage());
//    }

//    @Test
//    public void testUpdateFlight_NumberExists() {
//        Flight flight = new Flight();
//        flight.setFlightId(1L);
//        flight.setFlightNumber("123");
//
//        Flight existingFlight = new Flight();
//        existingFlight.setFlightId(2L);
//        existingFlight.setFlightNumber("123");
//
//        when(flightRepository.existsById(1L)).thenReturn(true);
//        when(flightRepository.existsByFlightNumber("123")).thenReturn(true);
//        when(flightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
//
//        APIException exception = assertThrows(APIException.class, () -> {
//            flightService.updateFlight(flight, 1L);
//        });
//
//        assertEquals("flight number already exists", exception.getMessage());
//    }

    @Test
    public void testDeleteFlight() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(1L);

        String result = flightService.deleteFlight(1L);

        assertEquals("Flight Deleted", result);
        verify(flightRepository).deleteById(1L);
    }

    @Test
    public void testDeleteFlight_NotFound() {
        when(flightRepository.existsById(1L)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            flightService.deleteFlight(1L);
        });

        assertEquals(AppConstants.FLIGHT_NOT_FOUND, exception.getMessage());
    }
}