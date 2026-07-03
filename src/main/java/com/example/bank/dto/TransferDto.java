package com.example.bank.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDto {
    private String fromType;
    private Integer fromId;
    private String onType;
    private Integer onId;
    private BigDecimal amount;


    public void setFromAccountId(String value) {
        if (value != null && value.contains("_")) {
            String[] parts = value.split("_");
            this.fromType = parts[0];
            this.fromId = Integer.parseInt(parts[1]);
        }
    }


    public void setOnAccountId(String value) {
        if (value != null && value.contains("_")) {
            String[] parts = value.split("_");
            this.onType = parts[0];
            this.onId = Integer.parseInt(parts[1]);
        }
    }

    // Геттеры для Thymeleaf (вызываются при рендеринге страницы)
    public String getFromAccountId() {
        if (fromType != null && fromId != null) {
            return fromType + "_" + fromId;
        }
        return null;
    }

    public String getOnAccountId() {
        if (onType != null && onId != null) {
            return onType + "_" + onId;
        }
        return null;
    }
}
