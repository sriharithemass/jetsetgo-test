package com.training.controllers;

import com.training.payload.TicketDTO;
import com.training.payload.TicketRequest;
import com.training.services.impl.TicketServiceImpl;
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

class TicketControllerTest {

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTicketsByUser() {
        TicketDTO ticketDTO = new TicketDTO();
        List<TicketDTO> ticketDTOS = Collections.singletonList(ticketDTO);
        String username = "testUser";

        when(ticketService.getTicketsByUser(username, 0, 50)).thenReturn(ticketDTOS);

        ResponseEntity<List<TicketDTO>> response = ticketController.getTicketsByUser(username, 0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTOS, response.getBody());
    }

    @Test
    void testGetAllTickets() {
        TicketDTO ticketDTO = new TicketDTO();
        List<TicketDTO> ticketDTOS = Collections.singletonList(ticketDTO);

        when(ticketService.getAllTickets(0, 50)).thenReturn(ticketDTOS);

        ResponseEntity<List<TicketDTO>> response = ticketController.getAllTickets(0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ticketDTOS, response.getBody());
    }

    @Test
    void testCreateTickets() {
        TicketRequest ticketRequest = new TicketRequest();
        ticketRequest.setPassengerIds(Collections.singletonList(1L));
        ticketRequest.setSelectedSeats(Collections.singletonList(1));
        String username = "testUser";
        Long bookingId = 1L;
        String message = "Tickets created";

        when(ticketService.createTickets(username, bookingId, ticketRequest.getPassengerIds(), ticketRequest.getSelectedSeats())).thenReturn(message);

        ResponseEntity<String> response = ticketController.createTickets(username, bookingId, ticketRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testDeleteTicket() {
        Long ticketId = 1L;
        String message = "Ticket deleted";

        when(ticketService.deleteTicket(ticketId)).thenReturn(message);

        ResponseEntity<String> response = ticketController.deleteTicket(ticketId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}