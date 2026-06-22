package com.example.bank.repository;

import com.example.bank.model.CreditUsersModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditUsersRepository extends JpaRepository<CreditUsersModel,Integer> {
    List<CreditUsersModel> findByUserId(int userId);

    CreditUsersModel findByIdAndUserId(int onId, int userId);

    Page<CreditUsersModel> findAll(Pageable pageable);
}
