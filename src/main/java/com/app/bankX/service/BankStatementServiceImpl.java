package com.app.bankX.service;


import com.app.bankX.dto.EmailDetails;
import com.app.bankX.entity.Transaction;
import com.app.bankX.entity.User;
import com.app.bankX.repository.TransactionRepository;
import com.app.bankX.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatementServiceImpl implements BankStatementService{

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private static final String FILE = "/Users/ashikadrolia/Documents/MyStatement.pdf";

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
//        return transactionRepository.findAll().stream()
//                .filter(transaction -> transaction.getCreatedAt().isAfter(start))
//                .filter(transaction -> transaction.getCreatedAt().isEqual(end))
//                .toList();

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> isDateWithinRange(transaction.getCreatedAt(), start, end))
                .collect(Collectors.toList());

        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        //now we are writing content to the document
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("BankX"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        PdfPCell bankAddress = new PdfPCell(new Phrase("Bangalore"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: "+ endDate));

        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() +" "+user.getLastName();

        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: "+ user.getAddress()));
        address.setBorder(0);

        //transaction table
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("Bank Account Statement")
                .messageBody("Please find attached your bank account statement")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);



        return transactionList;

    }

    @Override
    public void designStatement(List<Transaction> transactions) throws FileNotFoundException, DocumentException {






    }

    private boolean isDateWithinRange(LocalDate transactionDate, LocalDate startDate, LocalDate endDate) {
        return (transactionDate.isEqual(startDate) || transactionDate.isAfter(startDate)) &&
                (transactionDate.isEqual(endDate) || transactionDate.isBefore(endDate));
    }
}
