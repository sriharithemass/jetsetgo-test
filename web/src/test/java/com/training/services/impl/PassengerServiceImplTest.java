//package com.training.services.impl;
//
//import com.training.config.AppConstants;
//import com.training.exception.APIException;
//import com.training.models.Passenger;
//import com.training.repositories.PassengerRepository;
//import com.training.repositories.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class PassengerServiceImplTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private PassengerRepository passengerRepository;
//    @InjectMocks
//    private PassengerServiceImpl passengerService;
//
//
//    @Test
//    void getPassengersByUser_UserNotFound() {
//        String userName="nonExistUser";
//        when(userRepository.existsByUserName(userName)).thenReturn(false);
//        APIException exception=assertThrows(APIException.class,
//                ()->passengerService.getPassengersByUser(userName));
//        assertEquals(AppConstants.USER_NOT_FOUND,exception.getMessage());
//    }
//
//
//    @Test
//    void getPassengersByUser_NoPassengersFound(){
//        String userName="validUser";
//        when(userRepository.existsByUserName(userName)).thenReturn(true);
//        when(passengerRepository.findAllByUser_UserName(userName)).thenReturn(new ArrayList<>());
//        APIException exception=assertThrows(APIException.class,
//                ()->passengerService.getPassengersByUser(userName));
//        assertEquals("No passengers found",exception.getMessage());
//    }
//
//
//    @Test
//    void getPassengersByUser_PassengersFound() {
//        String userName="validUser";
//        List<Passenger> mockPassenger=new ArrayList<>();
//        mockPassenger.add(new Passenger(1L,"Passenger1",22,"male"));
//        mockPassenger.add(new Passenger(2L,"Passenger2",22,"male"));
//        when(userRepository.existsByUserName(userName)).thenReturn(true);
//        when(passengerRepository.findAllByUser_UserName(userName)).thenReturn(mockPassenger);
//        List<Passenger> passengers=passengerService.getPassengersByUser(userName);
//        assertNotNull(passengers);
//        assertEquals("Passenger1",passengers.getFirst().getPassengerName());
//    }
//
//    @Test
//    void getPassengerById() {
//        Passenger passenger = new Passenger(1L,"hari",22,"male");
//
//        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
//        Passenger result = passengerService.getPassengerById(1L);
//
//        assertEquals(passenger.getPassengerName(), result.getPassengerName());
//    }
//
//    @Test
//    void createPassenger() {
//
//    }
//
//    @Test
//    void updatePassenger() {
//    }
//
//    @Test
//    void deletePassenger() {
//    }
//}