package com.training.services.impl;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.Passenger;
import com.training.models.User;
import com.training.repositories.PassengerRepository;
import com.training.repositories.UserRepository;
import com.training.services.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Override
    public List<Passenger> getPassengersByUser(String username, Integer pageNumber, Integer pageSize) {
        if (!userRepository.existsByUserName(username))
            throw new APIException(AppConstants.USER_NOT_FOUND);
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize);
        List<Passenger> passengers = passengerRepository.findAllByUser_UserName(username, pageDetails).getContent();

        if (passengers.isEmpty())
            throw new APIException("No passengers found");

        return passengers;
    }

    @Override
    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(()-> new APIException(AppConstants.PASSENGER_NOT_FOUND));
    }

    @Override
    public String createPassenger(Passenger passenger, String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new APIException(AppConstants.USER_NOT_FOUND));

        passenger.setUser(user);
        passengerRepository.save(passenger);
        return "Passenger Added";
    }

    @Override
    public String updatePassenger(Passenger passenger, Long passengerId, String username) {
        if (!passengerRepository.existsById(passengerId))
            throw new APIException(AppConstants.PASSENGER_NOT_FOUND);

        User user = userRepository.findByUserName(username)
                        .orElseThrow(()-> new APIException(AppConstants.USER_NOT_FOUND));

        passenger.setPassengerId(passengerId);
        passenger.setUser(user);
        passengerRepository.save(passenger);

        return "Passenger updated";
    }

    @Override
    public String deletePassenger(Long passengerId) {
        if (!passengerRepository.existsById(passengerId))
            throw new APIException(AppConstants.PASSENGER_NOT_FOUND);

        passengerRepository.deleteById(passengerId);

        return "Passenger Removed";
    }
}
