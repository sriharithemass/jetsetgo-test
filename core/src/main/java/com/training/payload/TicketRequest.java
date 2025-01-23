package com.training.payload;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TicketRequest {

    @NotNull
    List<Long> passengerIds = new ArrayList<>();

    @NotNull
    List<Integer> selectedSeats = new ArrayList<>();

    public TicketRequest(List<Long> passengerIds, List<Integer> selectedSeats) {
        this.passengerIds = passengerIds;
        this.selectedSeats = selectedSeats;
    }

    public TicketRequest() {
    }

    public List<Long> getPassengerIds() {
        return this.passengerIds;
    }

    public List<Integer> getSelectedSeats() {
        return this.selectedSeats;
    }

    public void setPassengerIds(List<Long> passengerIds) {
        this.passengerIds = passengerIds;
    }

    public void setSelectedSeats(List<Integer> selectedSeats) {
        this.selectedSeats = selectedSeats;
    }
}
