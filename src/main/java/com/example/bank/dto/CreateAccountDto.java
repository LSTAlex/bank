package com.example.bank.dto;

import com.example.bank.model.ScoreTypeModel;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {
    @NotNull
    private ScoreTypeModel scoreTypeModel;
    private BigDecimal creditLimit;
}
