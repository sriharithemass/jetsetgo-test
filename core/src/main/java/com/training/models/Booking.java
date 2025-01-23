package com.training.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @ElementCollection
    @CollectionTable(name = "seat", joinColumns = {
            @JoinColumn(name = "booking_id", referencedColumnName = "bookingId")
    })
    @MapKeyColumn(name = "seat_number")
    @Column(name = "is_available")
    private Map<Integer, Boolean> seats = new HashMap<>();
    private Integer availableSeats;

    private String bookingStatus;

    @ManyToOne
    @JoinColumn(name = "flightId")
    private Flight flight;

    @OneToMany(mappedBy = "booking", orphanRemoval = false, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Ticket> tickets = new ArrayList<>();
}
