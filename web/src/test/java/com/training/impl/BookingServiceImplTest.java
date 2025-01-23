package com.training.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Booking;
import com.training.models.Flight;
import com.training.payload.BookingDTO;
import com.training.payload.BookingResponse;
import com.training.repositories.BookingRepository;
import com.training.services.impl.BookingServiceImpl;
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

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightServiceImpl flightService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testGetBookingsBySearch() {
//        Booking booking = new Booking();
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Booking> page = new PageImpl<>(Collections.singletonList(booking));
//
//        when(bookingRepository.getBookingsBySearch(any(LocalDate.class), anyString(), anyString(), eq(pageable))).thenReturn(page);
//
//        BookingResponse response = bookingService.getBookingsBySearch(LocalDate.now(), "NYC", "LAX", 0, 10);
//
//        assertNotNull(response);
//        assertEquals(1, response.getBookingDTOList().size());
//    }

//    @Test
//    public void testGetAllBookings() {
//        Booking booking = new Booking();
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Booking> page = new PageImpl<>(Collections.singletonList(booking));
//
//        when(bookingRepository.findAll(pageable)).thenReturn(page);
//
//        List<BookingDTO> result = bookingService.getAllBookings(0, 10);
//
//        assertEquals(1, result.size());
//    }

//    @Test
//    public void testGetBookingById() {
//        Booking booking = new Booking();
//        booking.setBookingId(1L);
//
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//
//        BookingDTO result = bookingService.getBookingById(1L);
//
//        assertNotNull(result);
//        assertEquals(1L, result.getBookingId());
//    }

    @Test
    public void testCreateBooking() {
        Booking booking = new Booking();
        Flight flight = new Flight();
        flight.setTotalSeats(100);

        when(flightService.getFlightById(1L)).thenReturn(flight);
        when(bookingRepository.save(booking)).thenReturn(booking);

        String result = bookingService.createBooking(booking, 1L);

        assertEquals("Booking Created", result);
        assertEquals(100, booking.getAvailableSeats());
    }

    @Test
    public void testUpdateBooking() {
        Booking booking = new Booking();
        booking.setBookingId(1L);
        Booking existingBooking = new Booking();
        existingBooking.setFlight(new Flight());

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        String result = bookingService.updateBooking(booking, 1L);

        assertEquals("Booking updated", result);
    }

    @Test
    public void testDeleteBooking() {
        when(bookingRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookingRepository).deleteById(1L);

        String result = bookingService.deleteBooking(1L);

        assertEquals("Booking Removed", result);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    public void testGetBookingsBySearch_NoBookingsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(Collections.emptyList());

        when(bookingRepository.getBookingsBySearch(any(LocalDate.class), anyString(), anyString(), eq(pageable))).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            bookingService.getBookingsBySearch(LocalDate.now(), "NYC", "LAX", 0, 10);
        });

        assertEquals("No bookings found", exception.getMessage());
    }

    @Test
    public void testGetAllBookings_NoBookingsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Booking> page = new PageImpl<>(Collections.emptyList());

        when(bookingRepository.findAll(pageable)).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            bookingService.getAllBookings(0, 10);
        });

        assertEquals("No bookings found", exception.getMessage());
    }

    @Test
    public void testGetBookingById_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            bookingService.getBookingById(1L);
        });

        assertEquals(AppConstants.BOOKING_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testDeleteBooking_BookingNotFound() {
        when(bookingRepository.existsById(1L)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            bookingService.deleteBooking(1L);
        });

        assertEquals(AppConstants.BOOKING_NOT_FOUND, exception.getMessage());
    }
}