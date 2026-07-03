package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdminAccountViewDto {
    private int id;
    private String username;
    private String type;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private Double interestRate;
}
