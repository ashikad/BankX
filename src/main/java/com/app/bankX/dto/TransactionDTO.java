package com.app.bankX.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionDTO {

    private String transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
