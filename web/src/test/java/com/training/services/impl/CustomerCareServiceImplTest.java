package com.training.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.CustomerCare;
import com.training.models.User;
import com.training.repositories.CustomerCareRepository;
import com.training.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

class CustomerCareServiceImplTest {

    @InjectMocks
    private CustomerCareServiceImpl customerCareService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerCareRepository customerCareRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomerCareMessage() {
        CustomerCare customerCare = new CustomerCare();
        User user = new User();
        user.setUserName("testUser");

        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));
        when(customerCareRepository.save(customerCare)).thenReturn(customerCare);

        String result = customerCareService.createCustomerCareMessage(customerCare, "testUser");

        assertEquals("Message Sent", result);
        verify(customerCareRepository).save(customerCare);
    }

    @Test
    void testGetCustomerCareMessagesByUser() {
        User user = new User();
        user.setUserName("testUser");
        CustomerCare customerCare = new CustomerCare();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerCare> page = new PageImpl<>(Collections.singletonList(customerCare));

        when(userRepository.existsByUserName("testUser")).thenReturn(true);
        when(customerCareRepository.findAllByUser_UserName("testUser", pageable)).thenReturn(page);

        List<CustomerCare> result = customerCareService.getCustomerCareMessagesByUser("testUser", 0, 10);

        assertEquals(1, result.size());
        assertEquals(customerCare, result.get(0));
    }

    @Test
    void testGetAllCustomerCareMessages() {
        CustomerCare customerCare = new CustomerCare();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerCare> page = new PageImpl<>(Collections.singletonList(customerCare));

        when(customerCareRepository.findAll(pageable)).thenReturn(page);

        List<CustomerCare> result = customerCareService.getAllCustomerCareMessages(0, 10);

        assertEquals(1, result.size());
        assertEquals(customerCare, result.get(0));
    }

    @Test
    void testDeleteCustomerCareMessageById() {
        when(customerCareRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerCareRepository).deleteById(1L);

        String result = customerCareService.deleteCustomerCareMessageById(1L);

        assertEquals("Message deleted", result);
        verify(customerCareRepository).deleteById(1L);
    }

    @Test
    void testCreateCustomerCareMessage_UserNotFound() {
        CustomerCare customerCare = new CustomerCare();

        when(userRepository.findByUserName("testUser")).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> {
            customerCareService.createCustomerCareMessage(customerCare, "testUser");
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetCustomerCareMessagesByUser_UserNotFound() {
        when(userRepository.existsByUserName("testUser")).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            customerCareService.getCustomerCareMessagesByUser("testUser", 0, 10);
        });

        assertEquals(AppConstants.USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testGetAllCustomerCareMessages_NoMessagesFound() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerCare> page = new PageImpl<>(Collections.emptyList());

        when(customerCareRepository.findAll(pageable)).thenReturn(page);

        APIException exception = assertThrows(APIException.class, () -> {
            customerCareService.getAllCustomerCareMessages(0, 10);
        });

        assertEquals("No messages found", exception.getMessage());
    }

    @Test
    void testDeleteCustomerCareMessageById_MessageNotFound() {
        when(customerCareRepository.existsById(1L)).thenReturn(false);

        APIException exception = assertThrows(APIException.class, () -> {
            customerCareService.deleteCustomerCareMessageById(1L);
        });

        assertEquals(AppConstants.CUSTOMER_CARE_MESSAGE_NOT_FOUND, exception.getMessage());
    }
}