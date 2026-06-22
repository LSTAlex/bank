package com.example.bank.repository;

import com.example.bank.model.SavingsUsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SavingsUserRpository extends JpaRepository<SavingsUsersModel,Integer> {
    List<SavingsUsersModel> findByUserId(int userId);

    SavingsUsersModel findByIdAndUserId(int onId, int userId);
    Page<SavingsUsersModel> findAll(Pageable pageable);
}
