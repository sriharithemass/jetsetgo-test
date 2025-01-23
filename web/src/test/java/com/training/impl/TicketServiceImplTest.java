package com.training.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Booking;
import com.training.models.Passenger;
import com.training.models.Ticket;
import com.training.models.User;
import com.training.payload.TicketBookingDTO;
import com.training.payload.TicketDTO;
import com.training.repositories.BookingRepository;
import com.training.repositories.PassengerRepository;
import com.training.repositories.TicketRepository;
import com.training.repositories.UserRepository;
import com.training.services.impl.BookingServiceImpl;
import com.training.services.impl.TicketServiceImpl;
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
import java.util.Map;
import java.util.Optional;

public class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private BookingServiceImpl bookingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTicketsByUser() {
        Ticket ticket = new Ticket();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(Collections.singletonList(ticket));

        when(ticketRepository.findAllByUser_UserName("testUser", pageable)).thenReturn(page);

        List<TicketDTO> result = ticketService.getTicketsByUser("testUser", 0, 10);

        assertEquals(1, result.size());
    }

    @Test
    public void testGetAllTickets() {
        Ticket ticket = new Ticket();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(Collections.singletonList(ticket));

        when(ticketRepository.findAll(pageable)).thenReturn(page);

        List<TicketDTO> result = ticketService.getAllTickets(0, 10);

        assertEquals(1, result.size());
    }

//    @Test
//    public void testCreateTickets() {
//        User user = new User();
//        Booking booking = new Booking();
//        booking.setAvailableSeats(10);
//        booking.setSeats(Map.of(1, true, 2, true));
//        Passenger passenger = new Passenger();
//
//        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
//        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
//        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
//        when(ticketRepository.save(any(Ticket.class))).thenReturn(new Ticket());
//
//        String result = ticketService.createTickets("testUser", 1L, List.of(1L), List.of(1));
//
//        assertEquals("Tickets Generated", result);
//    }

    @Test
    public void testDeleteTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        String result = ticketService.deleteTicket(1L);

        assertEquals("Ticket Deleted", result);
        verify(ticketRepository).deleteById(1L);
    }

    @Test
    public void testGetTicketsByUser_NoTicketsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(Collections.emptyList());

        when(ticketRepository.findAllByUser_UserName("testUser", pageable)).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.getTicketsByUser("testUser", 0, 10);
        });

        assertEquals("No tickets found", exception.getMessage());
    }

    @Test
    public void testGetAllTickets_NoTicketsFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ticket> page = new PageImpl<>(Collections.emptyList());

        when(ticketRepository.findAll(pageable)).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.getAllTickets(0, 10);
        });

        assertEquals("No tickets found", exception.getMessage());
    }

    @Test
    public void testCreateTickets_UserNotFound() {
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.createTickets("testUser", 1L, List.of(1L), List.of(1));
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testCreateTickets_BookingNotFound() {
        User user = new User();
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.createTickets("testUser", 1L, List.of(1L), List.of(1));
        });

        assertEquals(AppConstants.BOOKING_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void testCreateTickets_BookingClosed() {
        User user = new User();
        Booking booking = new Booking();
        booking.setAvailableSeats(0);

        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.createTickets("testUser", 1L, List.of(1L), List.of(1));
        });

        assertEquals(AppConstants.BOOKING_CLOSED, exception.getMessage());
    }

    @Test
    public void testDeleteTicket_NotFound() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            ticketService.deleteTicket(1L);
        });

        assertEquals(AppConstants.TICKET_NOT_FOUND, exception.getMessage());
    }
}