package com.app.bankX.service;

import com.app.bankX.entity.Transaction;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.util.List;

public interface BankStatementService {

    List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException;

    public void designStatement(List<Transaction> transactions) throws FileNotFoundException, DocumentException;
}
