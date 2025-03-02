package com.app.bankX.service;

import com.app.bankX.dto.TransactionDTO;
import com.app.bankX.entity.Transaction;
import com.app.bankX.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDTO.getTransactionType())
                .accountNumber(transactionDTO.getAccountNumber())
                .amount(transactionDTO.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);
        System.out.println("Transaction details saved successfully");
    }
}
