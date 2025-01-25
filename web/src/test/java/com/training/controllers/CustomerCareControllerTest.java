package com.training.controllers;

import com.training.models.CustomerCare;
import com.training.services.impl.CustomerCareServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerCareControllerTest {

    @InjectMocks
    private CustomerCareController customerCareController;

    @Mock
    private CustomerCareServiceImpl customerCareService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMessage() {
        CustomerCare customerCareMessage = new CustomerCare();
        String username = "testUser";
        String message = "Request created";

        when(customerCareService.createCustomerCareMessage(customerCareMessage, username)).thenReturn(message);

        ResponseEntity<String> response = customerCareController.createMessage(customerCareMessage, username);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(message, response.getBody());
    }

    @Test
    void testGetAllMessagesByUser() {
        CustomerCare customerCareMessage = new CustomerCare();
        List<CustomerCare> customerCareMessages = Collections.singletonList(customerCareMessage);
        String username = "testUser";

        when(customerCareService.getCustomerCareMessagesByUser(username, 0, 50)).thenReturn(customerCareMessages);

        ResponseEntity<List<CustomerCare>> response = customerCareController.getAllMessagesByUser(username, 0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerCareMessages, response.getBody());
    }

    @Test
    void testGetAllMessages() {
        CustomerCare customerCareMessage = new CustomerCare();
        List<CustomerCare> customerCareMessages = Collections.singletonList(customerCareMessage);

        when(customerCareService.getAllCustomerCareMessages(0, 50)).thenReturn(customerCareMessages);

        ResponseEntity<List<CustomerCare>> response = customerCareController.getAllMessages(0, 50);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerCareMessages, response.getBody());
    }

    @Test
    void testDeleteMessageById() {
        Long messageId = 1L;
        String message = "Request deleted";

        when(customerCareService.deleteCustomerCareMessageById(messageId)).thenReturn(message);

        ResponseEntity<String> response = customerCareController.deleteMessageById(messageId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(message, response.getBody());
    }
}