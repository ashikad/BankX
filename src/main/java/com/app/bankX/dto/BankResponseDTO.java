package com.app.bankX.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankResponseDTO {

    private String responseCode;
    private String responseMsg;
    private AccountInfo accountInfo;
}
