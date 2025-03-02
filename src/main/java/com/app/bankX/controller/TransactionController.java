package com.app.bankX.controller;

import com.app.bankX.entity.Transaction;
import com.app.bankX.service.BankStatementServiceImpl;
import com.itextpdf.text.DocumentException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/bankStatement")
@AllArgsConstructor
public class TransactionController {


    private BankStatementServiceImpl bankStatementServiceImpl;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatementServiceImpl.generateStatement(accountNumber,startDate,endDate);
    }
}
