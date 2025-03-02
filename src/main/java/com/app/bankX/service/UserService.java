package com.app.bankX.service;

import com.app.bankX.dto.*;

public interface UserService {

    BankResponseDTO createAccount(UserRequestDTO userRequest);

    BankResponseDTO balancyEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponseDTO creditAccount(CreditDebitRequest creditRequest);

    BankResponseDTO debitAccount(CreditDebitRequest debitRequest);

    BankResponseDTO transferAmount(TransferRequest transferRequest);

    // BankResponseDTO getAccountInfo(int userId);
}
