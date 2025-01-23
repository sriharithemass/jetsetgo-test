package com.training.controllers;

import com.training.models.CustomerCare;
import com.training.services.impl.CustomerCareServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerCareController {

    @Autowired
    private CustomerCareServiceImpl customerCareService;

    @PostMapping("/customer-care")
    public ResponseEntity<String> createMessage(
            @RequestBody CustomerCare customerCareMessage,
            @RequestHeader("username") String username
    ){
        String message = customerCareService.createCustomerCareMessage(customerCareMessage,username);

        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/customer-care")
    public ResponseEntity<List<CustomerCare>> getAllMessagesByUser(
            @RequestHeader("username") String username,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize
            ){
        List<CustomerCare> customerCareMessages = customerCareService.getCustomerCareMessagesByUser(username, pageNumber, pageSize);

        return new ResponseEntity<>(customerCareMessages, HttpStatus.OK);
    }

    @GetMapping("/admin/all-customer-messages")
    public ResponseEntity<List<CustomerCare>> getAllMessages(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "50") Integer pageSize
    ){
        List<CustomerCare> customerCareMessages = customerCareService.getAllCustomerCareMessages(pageNumber, pageSize);

        return new ResponseEntity<>(customerCareMessages, HttpStatus.OK);
    }

    @DeleteMapping("/admin/customer-care/{messageId}")
    public ResponseEntity<String> deleteMessageById(@PathVariable Long messageId){
        String message = customerCareService.deleteCustomerCareMessageById(messageId);

        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}