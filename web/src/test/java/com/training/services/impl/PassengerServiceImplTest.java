package com.training.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Passenger;
import com.training.models.User;
import com.training.repositories.PassengerRepository;
import com.training.repositories.UserRepository;
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

class PassengerServiceImplTest {

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPassengersByUser() {
        String username = "testUser";
        Passenger passenger = new Passenger();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Passenger> page = new PageImpl<>(Collections.singletonList(passenger));

        when(userRepository.existsByUserName(username)).thenReturn(true);
        when(passengerRepository.findAllByUser_UserName(username, pageable)).thenReturn(page);

        List<Passenger> result = passengerService.getPassengersByUser(username, 0, 10);

        assertEquals(1, result.size());
        assertEquals(passenger, result.get(0));
    }

    @Test
    void testGetPassengersByUser_UserNotFound() {
        String username = "testUser";

        when(userRepository.existsByUserName(username)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.getPassengersByUser(username, 0, 10);
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetPassengerById() {
        Long passengerId = 1L;
        Passenger passenger = new Passenger();

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        Passenger result = passengerService.getPassengerById(passengerId);

        assertEquals(passenger, result);
    }

    @Test
    void testGetPassengerById_PassengerNotFound() {
        Long passengerId = 1L;

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.getPassengerById(passengerId);
        });

        assertEquals(AppConstants.PASSENGER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreatePassenger() {
        Passenger passenger = new Passenger();
        String username = "testUser";
        User user = new User();
        user.setUserName(username);

        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        String result = passengerService.createPassenger(passenger, username);

        assertEquals("Passenger Added", result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void testCreatePassenger_UserNotFound() {
        Passenger passenger = new Passenger();
        String username = "testUser";

        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.createPassenger(passenger, username);
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testUpdatePassenger() {
        Passenger passenger = new Passenger();
        Long passengerId = 1L;
        String username = "testUser";
        User user = new User();
        user.setUserName(username);

        when(passengerRepository.existsById(passengerId)).thenReturn(true);
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(user));
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        String result = passengerService.updatePassenger(passenger, passengerId, username);

        assertEquals("Passenger updated", result);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void testUpdatePassenger_PassengerNotFound() {
        Passenger passenger = new Passenger();
        Long passengerId = 1L;
        String username = "testUser";

        when(passengerRepository.existsById(passengerId)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.updatePassenger(passenger, passengerId, username);
        });

        assertEquals(AppConstants.PASSENGER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testUpdatePassenger_UserNotFound() {
        Passenger passenger = new Passenger();
        Long passengerId = 1L;
        String username = "testUser";

        when(passengerRepository.existsById(passengerId)).thenReturn(true);
        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.updatePassenger(passenger, passengerId, username);
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeletePassenger() {
        Long passengerId = 1L;

        when(passengerRepository.existsById(passengerId)).thenReturn(true);
        doNothing().when(passengerRepository).deleteById(passengerId);

        String result = passengerService.deletePassenger(passengerId);

        assertEquals("Passenger Removed", result);
        verify(passengerRepository).deleteById(passengerId);
    }

    @Test
    void testDeletePassenger_PassengerNotFound() {
        Long passengerId = 1L;

        when(passengerRepository.existsById(passengerId)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            passengerService.deletePassenger(passengerId);
        });

        assertEquals(AppConstants.PASSENGER_NOT_FOUND, exception.getMessage());
    }
}