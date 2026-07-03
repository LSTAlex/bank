package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountOperationDto {
    private Integer accountId;
    private BigDecimal amount;
}
