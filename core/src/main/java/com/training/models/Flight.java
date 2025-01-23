package com.training.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    @NotBlank
    private String flightName;
    @NotBlank
    private String flightNumber;
    @NotBlank
    private String flightType;
    @NotBlank
    private String departureLocation;
    @NotBlank
    private String arrivalLocation;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;

    @NotNull
    private Integer totalSeats;
    @NotNull
    private Double flightPrice;

    @OneToMany(mappedBy = "flight", orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();
}
