package com.training.services.impl;

import com.training.config.AppConstants;
import com.training.exception.APIException;
import com.training.models.CustomerCare;
import com.training.models.User;
import com.training.repositories.CustomerCareRepository;
import com.training.repositories.UserRepository;
import com.training.services.CustomerCareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerCareServiceImpl implements CustomerCareService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerCareRepository customerCareRepository;

    @Override
    public String createCustomerCareMessage(CustomerCare customerCareMessage, String username) {
        User user = userRepository.findByUserName(username)
                        .orElseThrow(()-> new  APIException(AppConstants.USER_NOT_FOUND));

        customerCareMessage.setUser(user);
        customerCareRepository.save(customerCareMessage);

        return "Message Sent";
    }

    @Override
    public List<CustomerCare> getCustomerCareMessagesByUser(String username, Integer pageNumber, Integer pageSize) {
        if (!userRepository.existsByUserName(username))
            throw new APIException(AppConstants.USER_NOT_FOUND);

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);

        Page<CustomerCare> customerCareMessagesPage = customerCareRepository.findAllByUser_UserName(username, pageDetails);
        List<CustomerCare> customerCareMessages = customerCareMessagesPage.getContent();
        if (customerCareMessages.isEmpty())
            throw new APIException("No messages found");

        return customerCareMessages;
    }

    @Override
    public List<CustomerCare> getAllCustomerCareMessages(Integer pageNumber, Integer pageSize) {
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<CustomerCare> customerCareMessagesPage = customerCareRepository.findAll(pageDetails);
        List<CustomerCare> customerCareMessages = customerCareMessagesPage.getContent();

        if (customerCareMessages.isEmpty())
            throw new APIException("No messages found");

        return customerCareMessages;
    }

    @Override
    public String deleteCustomerCareMessageById(Long messageId) {
        if (!customerCareRepository.existsById(messageId))
            throw new APIException(AppConstants.CUSTOMER_CARE_MESSAGE_NOT_FOUND);

        customerCareRepository.deleteById(messageId);

        return "Message deleted";
    }
}
