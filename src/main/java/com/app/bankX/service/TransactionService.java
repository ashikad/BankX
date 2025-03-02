package com.app.bankX.service;

import com.app.bankX.dto.TransactionDTO;
import com.app.bankX.entity.Transaction;

public interface TransactionService {

    void saveTransaction(TransactionDTO transactionDTO);
}
