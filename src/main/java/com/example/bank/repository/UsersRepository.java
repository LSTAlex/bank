package com.example.bank.repository;

import com.example.bank.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersModel, Integer> {

    UsersModel findByUsername(String username);

}
