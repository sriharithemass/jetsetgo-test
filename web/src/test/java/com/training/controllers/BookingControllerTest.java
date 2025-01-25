package com.training.controllers;

import com.training.models.Booking;
import com.training.payload.BookingDTO;
import com.training.payload.BookingResponse;
import com.training.services.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookingsBySearch() {
        LocalDate departureDate = LocalDate.now();
        String departureLocation = "LocationA";
        String arrivalLocation = "LocationB";
        BookingResponse bookingResponse = new BookingResponse();

        when(bookingService.getBookingsBySearch(departureDate, departureLocation, arrivalLocation, 0, 50)).thenReturn(bookingResponse);

        ResponseEntity<BookingResponse> response = bookingController.getBookingsBySearch(departureDate, departureLocation, arrivalLocation, 0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingResponse, response.getBody());
    }

    @Test
    void testGetAllBookings() {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookingDTOList = Collections.singletonList(bookingDTO);

        when(bookingService.getAllBookings(0, 50)).thenReturn(bookingDTOList);

        ResponseEntity<List<BookingDTO>> response = bookingController.getAllBookings(0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTOList, response.getBody());
    }

    @Test
    void testGetBookingById() {
        Long bookingId = 1L;
        BookingDTO bookingDTO = new BookingDTO();

        when(bookingService.getBookingById(bookingId)).thenReturn(bookingDTO);

        ResponseEntity<BookingDTO> response = bookingController.getBookingById(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookingDTO, response.getBody());
    }

    @Test
    void testCreateBooking() {
        Booking booking = new Booking();
        Long flightId = 1L;
        String message = "Booking created";

        when(bookingService.createBooking(booking, flightId)).thenReturn(message);

        ResponseEntity<String> response = bookingController.createBooking(booking, flightId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testUpdateBooking() {
        Long bookingId = 1L;
        Booking booking = new Booking();
        String message = "Booking updated";

        when(bookingService.updateBooking(booking, bookingId)).thenReturn(message);

        ResponseEntity<String> response = bookingController.updateBooking(booking, bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testDeleteBooking() {
        Long bookingId = 1L;
        String message = "Booking deleted";

        when(bookingService.deleteBooking(bookingId)).thenReturn(message);

        ResponseEntity<String> response = bookingController.deleteBooking(bookingId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}