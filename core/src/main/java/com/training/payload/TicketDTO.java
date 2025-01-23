package com.training.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.training.models.Passenger;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TicketDTO {
    private TicketBookingDTO ticketBookingDTO;
    private Passenger passenger;
    private Long ticketId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingTime;
    private Integer seatNumber;
}
