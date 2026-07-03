package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAccountViewDto {
    private int id;
    private String type; //DEBIT,CREDIT,SAVINGS
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private Double interestRate;
}
