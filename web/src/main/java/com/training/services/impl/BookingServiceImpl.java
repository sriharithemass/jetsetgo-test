package com.training.services.impl;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Booking;
import com.training.models.Flight;
import com.training.payload.BookingDTO;
import com.training.payload.BookingResponse;
import com.training.payload.TicketBookingDTO;
import com.training.repositories.BookingRepository;
import com.training.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightServiceImpl flightService;

    @Override
    public BookingResponse getBookingsBySearch(LocalDate departureDate, String departureLocation, String arrivalLocation, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        Page<Booking> bookingPage = bookingRepository.getBookingsBySearch(departureDate, departureLocation, arrivalLocation, pageDetails);

        List<Booking> bookings = bookingPage.getContent();

        if (bookings.isEmpty())
            throw new APIException("No bookings found");

        List<BookingDTO> bookingDTOList = bookings.stream()
                .map(this::bookingToDto).toList();

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setBookingDTOList(bookingDTOList);
        bookingResponse.setPageNumber(bookingPage.getNumber());
        bookingResponse.setPageSize(bookingPage.getSize());
        bookingResponse.setTotalPages(bookingPage.getTotalPages());
        bookingResponse.setLastPage(bookingPage.isLast());

        return bookingResponse;
    }

    @Override
    public List<BookingDTO> getAllBookings(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        List<Booking> bookings = bookingRepository.findAll(pageDetails).getContent();

        if (bookings.isEmpty())
            throw new APIException("No bookings found");

        List<BookingDTO> bookingDTOList= bookings.stream().map(this::bookingToDto).toList();

        return bookingDTOList;
    }

    @Override
    public BookingDTO getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()->new APIException(AppConstants.BOOKING_NOT_FOUND));

        BookingDTO bookingDTO = bookingToDto(booking);
        return bookingDTO;
    }

    @Override
    public String createBooking(Booking booking, Long flightId) {
        Flight flight = flightService.getFlightById(flightId);

        Map<Integer, Boolean> seats = new HashMap<>();

        IntStream.rangeClosed(1, flight.getTotalSeats())
                .forEach(i-> seats.put(i, true));

        booking.setFlight(flight);
        booking.setAvailableSeats(flight.getTotalSeats());
        booking.setSeats(seats);
        booking.setBookingStatus(AppConstants.BOOKING_OPEN);
        bookingRepository.save(booking);

        return "Booking Created";
    }

    @Override
    public String updateBooking(Booking booking, Long bookingId) {
        Booking booking1 = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new APIException(AppConstants.BOOKING_NOT_FOUND));

        booking.setFlight(booking1.getFlight());
        booking.setBookingId(bookingId);
        bookingRepository.save(booking);

        return "Booking updated";
    }

    @Override
    public String deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId))
            throw new APIException(AppConstants.BOOKING_NOT_FOUND);

        bookingRepository.deleteById(bookingId);

        return "Booking Removed";
    }

    public BookingDTO bookingToDto(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();

        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        bookingDTO.setDepartureDate(booking.getDepartureDate());
        bookingDTO.setArrivalDate(booking.getArrivalDate());
        bookingDTO.setSeats(booking.getSeats());
        bookingDTO.setAvailableSeats(booking.getAvailableSeats());

        bookingDTO.setFlightName(booking.getFlight().getFlightName());
        bookingDTO.setFlightNumber(booking.getFlight().getFlightNumber());
        bookingDTO.setFlightType(booking.getFlight().getFlightType());
        bookingDTO.setDepartureLocation(booking.getFlight().getDepartureLocation());
        bookingDTO.setArrivalLocation(booking.getFlight().getArrivalLocation());
        bookingDTO.setDepartureTime(booking.getFlight().getDepartureTime());
        bookingDTO.setArrivalTime(booking.getFlight().getArrivalTime());
        bookingDTO.setTotalSeats(booking.getFlight().getTotalSeats());
        bookingDTO.setFlightPrice(booking.getFlight().getFlightPrice());

        return bookingDTO;
    }

    public TicketBookingDTO ticketBookingToDto(Booking booking){
        TicketBookingDTO ticketBookingDTO = new TicketBookingDTO();

        ticketBookingDTO.setBookingId(booking.getBookingId());
        ticketBookingDTO.setBookingStatus(booking.getBookingStatus());
        ticketBookingDTO.setDepartureDate(booking.getDepartureDate());
        ticketBookingDTO.setArrivalDate(booking.getArrivalDate());
        ticketBookingDTO.setAvailableSeats(booking.getAvailableSeats());

        ticketBookingDTO.setFlightName(booking.getFlight().getFlightName());
        ticketBookingDTO.setFlightNumber(booking.getFlight().getFlightNumber());
        ticketBookingDTO.setFlightType(booking.getFlight().getFlightType());
        ticketBookingDTO.setDepartureLocation(booking.getFlight().getDepartureLocation());
        ticketBookingDTO.setArrivalLocation(booking.getFlight().getArrivalLocation());
        ticketBookingDTO.setDepartureTime(booking.getFlight().getDepartureTime());
        ticketBookingDTO.setArrivalTime(booking.getFlight().getArrivalTime());
        ticketBookingDTO.setTotalSeats(booking.getFlight().getTotalSeats());
        ticketBookingDTO.setFlightPrice(booking.getFlight().getFlightPrice());

        return ticketBookingDTO;
    }
}
