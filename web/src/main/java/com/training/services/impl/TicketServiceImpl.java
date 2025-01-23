package com.training.services.impl;

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
import com.training.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private BookingServiceImpl bookingService;

    @Override
    public List<TicketDTO> getTicketsByUser(String username, Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);

        List<Ticket> tickets = ticketRepository.findAllByUser_UserName(username, pageDetails).getContent();
        if (tickets.isEmpty())
            throw new APIException("No tickets found");

        List<TicketDTO> ticketDTOS = tickets.stream().map(this::ticketToDto).toList();

        return ticketDTOS;
    }

    @Override
    public List<TicketDTO> getAllTickets(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        List<Ticket> tickets = ticketRepository.findAll(pageDetails).getContent();
        if (tickets.isEmpty())
            throw new APIException("No tickets found");

        List<TicketDTO> ticketDTOS = tickets.stream().map(this::ticketToDto).toList();

        return ticketDTOS;
    }

    @Override
    public String createTickets(String username, Long bookingId, List<Long> passengerIds, List<Integer> selectedSeats) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new APIException(AppConstants.USER_NOT_FOUND));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new APIException(AppConstants.BOOKING_NOT_FOUND));

        if (booking.getAvailableSeats() == 0)
            throw new APIException(AppConstants.BOOKING_CLOSED);

        System.out.println(passengerIds);

        if (passengerIds.isEmpty())
            throw new APIException("Please select at-least one passenger");

        if (passengerIds.size()>booking.getAvailableSeats())
            throw new APIException("Not enough tickets: Number of selected passengers-"+passengerIds.size()+", Available Seats-"+booking.getAvailableSeats());

        Map<Integer, Boolean> seats = booking.getSeats();

        selectedSeats.forEach(seat-> {
            if (!seats.containsKey(seat))
                throw new APIException("Seat number Invalid");

            if (!seats.get(seat))
                throw new APIException("Seat not available");
        });

        selectedSeats.forEach(seat-> seats.put(seat, false));
        booking.setSeats(seats);

        int i = 0;
        for (Long passengerId : passengerIds){
            Passenger passenger = passengerRepository.findById(passengerId)
                    .orElseThrow(()-> new APIException(AppConstants.PASSENGER_NOT_FOUND));

            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setUser(user);
            ticket.setPassenger(passenger);
            booking.setAvailableSeats(booking.getAvailableSeats()-1);

            ticket.setSeatNumber(selectedSeats.get(i));
            ticketRepository.save(ticket);

            i++;
        }

        if (booking.getAvailableSeats() == 0)
            booking.setBookingStatus(AppConstants.BOOKING_CLOSED);
        else if(booking.getAvailableSeats() <= 20)
            booking.setBookingStatus(AppConstants.BOOKING_FILLING_FAST);

        bookingRepository.save(booking);

        return "Tickets Generated";
    }

    @Override
    public String deleteTicket(Long ticketId) {
        if (!ticketRepository.existsById(ticketId))
            throw new APIException(AppConstants.TICKET_NOT_FOUND);

        ticketRepository.deleteById(ticketId);

        return "Ticket Deleted";
    }

    public TicketDTO ticketToDto(Ticket ticket){
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId(ticket.getTicketId());

        Booking booking = ticket.getBooking();
        TicketBookingDTO ticketBookingDTO = bookingService.ticketBookingToDto(booking);
        ticketDTO.setTicketBookingDTO(ticketBookingDTO);
        ticketDTO.setPassenger(ticket.getPassenger());
        ticketDTO.setBookingTime(ticket.getBookingTime());
        ticketDTO.setSeatNumber(ticket.getSeatNumber());

        return ticketDTO;
    }
}
