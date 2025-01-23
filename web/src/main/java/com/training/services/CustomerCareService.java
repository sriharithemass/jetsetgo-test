package com.training.services;

import com.training.models.CustomerCare;

import java.util.List;

public interface CustomerCareService {
    String createCustomerCareMessage(CustomerCare customerCareMessage, String username);
    List<CustomerCare> getCustomerCareMessagesByUser(String username, Integer pageNumber, Integer pageSize);
    List<CustomerCare> getAllCustomerCareMessages(Integer pageNumber, Integer pageSize);
    String deleteCustomerCareMessageById(Long messageId);
}
