package com.training.repositories;

import com.training.models.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    @Query(value = "SELECT b FROM Booking b WHERE b.departureDate=:depDate AND b.flight.departureLocation=:depLoc AND b.flight.arrivalLocation=:arrLoc ORDER BY b.flight.departureTime")
    Page<Booking> getBookingsBySearch(@Param("depDate")LocalDate departureDate,@Param("depLoc") String departureLocation,@Param("arrLoc") String arrivalLocation, Pageable pageable);
}
