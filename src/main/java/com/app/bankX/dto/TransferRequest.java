package com.app.bankX.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransferRequest {

    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal amount;
}
