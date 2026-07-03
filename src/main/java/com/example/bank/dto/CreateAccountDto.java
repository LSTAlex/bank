package com.example.bank.dto;

import com.example.bank.model.ScoreTypeModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {
    private ScoreTypeModel scoreType;
    private BigDecimal creditLimit;
}
