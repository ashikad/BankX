package com.app.bankX.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreditDebitRequest {

    private String accountNumber;
    private BigDecimal amount;
}
