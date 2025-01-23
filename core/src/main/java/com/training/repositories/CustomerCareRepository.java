package com.training.repositories;

import com.training.models.CustomerCare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCareRepository extends JpaRepository<CustomerCare,Long> {
    Page<CustomerCare> findAllByUser_UserName(String username, Pageable pageDetails);
}
