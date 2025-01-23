package com.training.payload;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookingDTO {
    private Long bookingId;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private Integer availableSeats;
    private Map<Integer, Boolean> seats= new HashMap<>();
    private String bookingStatus;

    private String flightName;
    private String flightNumber;
    private String flightType;
    private String departureLocation;
    private String arrivalLocation;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Integer totalSeats;
    private Double flightPrice;
}
