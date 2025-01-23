package com.training.controllers;

import com.training.models.Booking;
import com.training.payload.BookingDTO;
import com.training.payload.BookingResponse;
import com.training.services.impl.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    private BookingServiceImpl bookingService;

    @GetMapping("/search-bookings")
    public ResponseEntity<BookingResponse> getBookingsBySearch(
            @RequestParam LocalDate departureDate,
            @RequestParam String departureLocation,
            @RequestParam String arrivalLocation,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize
            ){

        BookingResponse bookingResponse = bookingService.getBookingsBySearch(departureDate,departureLocation,arrivalLocation,pageNumber,pageSize);
        return new ResponseEntity<>(bookingResponse, HttpStatus.OK);
    }

    @GetMapping("admin/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize
    ){
        List<BookingDTO> bookingDTOList = bookingService.getAllBookings(pageNumber, pageSize);
        return new ResponseEntity<>(bookingDTOList,HttpStatus.OK);
    }

    @GetMapping("admin/bookings/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long bookingId){
        BookingDTO bookingDTO = bookingService.getBookingById(bookingId);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @PostMapping("/admin/bookings/{flightId}")
    public ResponseEntity<String> createBooking(@RequestBody Booking booking, @PathVariable Long flightId){
        String message = bookingService.createBooking(booking,flightId);
        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }

    @PutMapping("/admin/bookings/{bookingId}")
    public ResponseEntity<String> updateBooking(@RequestBody Booking booking, @PathVariable Long bookingId){
        String message = bookingService.updateBooking(booking,bookingId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @DeleteMapping("/admin/bookings/{bookingId}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long bookingId){
        String message = bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
