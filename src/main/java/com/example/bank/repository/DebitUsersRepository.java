package com.example.bank.repository;

import com.example.bank.model.DebitUsersModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DebitUsersRepository extends JpaRepository<DebitUsersModel,Integer> {
    List<DebitUsersModel> findByUserId(int userId);
    DebitUsersModel findByIdAndUserId(int id, int userId);

    Page<DebitUsersModel> findAll(Pageable pageable);
}
