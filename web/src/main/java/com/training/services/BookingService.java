package com.training.services;

import com.training.models.Booking;
import com.training.payload.BookingDTO;
import com.training.payload.BookingResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    BookingResponse getBookingsBySearch(LocalDate departureDate, String departureLocation, String arrivalLocation, Integer pageNumber, Integer pageSize);
    List<BookingDTO> getAllBookings(Integer pageNumber, Integer pageSize);
    BookingDTO getBookingById(Long bookingId);
    String createBooking(Booking booking, Long flightId);
    String updateBooking(Booking booking, Long bookingId);
    String deleteBooking(Long bookingId);
}
