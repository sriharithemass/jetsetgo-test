package com.training.services;

import com.training.models.Passenger;
import com.training.payload.TicketDTO;

import java.util.List;

public interface TicketService {
    List<TicketDTO> getTicketsByUser(String username, Integer pageNumber, Integer pageSize);
    List<TicketDTO> getAllTickets(Integer pageNumber, Integer pageSize);
    String createTickets(String username, Long bookingId, List<Long> passengerIds, List<Integer> selectedSeats);
    String deleteTicket(Long ticketId);
}
