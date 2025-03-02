package com.app.bankX.service;

import com.app.bankX.dto.*;
import com.app.bankX.entity.Transaction;
import com.app.bankX.entity.User;
import com.app.bankX.repository.UserRepository;
import com.app.bankX.utils.AccountStatus;
import com.app.bankX.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;


    private final EmailService emailService;
    private final TransactionService transactionService;

    private final PasswordEncoder passwordEncoder;

//    public UserServiceImpl(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }


    @Override
    public BankResponseDTO createAccount(UserRequestDTO userRequest) {
        //creating an account - saving a new user in db

        if(userRepository.existsByEmail(userRequest.getEmail())){

            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .status(AccountStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(userRequest.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account has been successfully created.\nYour account details - " +
                        "\nAccount Name : "+ savedUser.getFirstName()+" "+savedUser.getLastName() + "\nAccount Number : "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMsg(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() )
                        .build())
                .build();

    }

    @Override
    public BankResponseDTO balancyEnquiry(EnquiryRequest enquiryRequest) {
        //Check if the account exists in the db using account number
        Boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else{
            User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!isAccountExists){
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }else{
            User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return foundUser.getFirstName()+" "+foundUser.getLastName();
        }
    }

    @Override
    public BankResponseDTO creditAccount(CreditDebitRequest creditRequest) {

        Boolean isAccountExists = userRepository.existsByAccountNumber(creditRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else{
            User foundUser = userRepository.findByAccountNumber(creditRequest.getAccountNumber());
            foundUser.setAccountBalance(foundUser.getAccountBalance().add(creditRequest.getAmount()));

            userRepository.save(foundUser);

            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionType("CREDIT")
                    .accountNumber(foundUser.getAccountNumber())
                    .amount(creditRequest.getAmount())
                    .build();

            transactionService.saveTransaction(transactionDTO);

            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();


        }

    }

    @Override
    public BankResponseDTO debitAccount(CreditDebitRequest debitRequest) {
        Boolean isAccountExists = userRepository.existsByAccountNumber(debitRequest.getAccountNumber());
        if(!isAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else{
            User foundUser = userRepository.findByAccountNumber(debitRequest.getAccountNumber());
            if(foundUser.getAccountBalance().compareTo(debitRequest.getAmount())<0){
                return BankResponseDTO.builder()
                        .responseCode(AccountUtils.ACCOUNT_DEBITED_FAILURE_CODE)
                        .responseMsg(AccountUtils.ACCOUNT_DEBITED_FAILURE_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                                .accountNumber(foundUser.getAccountNumber())
                                .accountBalance(foundUser.getAccountBalance())
                                .build())
                        .build();
            }

            foundUser.setAccountBalance(foundUser.getAccountBalance().subtract(debitRequest.getAmount()));
            userRepository.save(foundUser);

            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .transactionType("DEBIT")
                    .accountNumber(foundUser.getAccountNumber())
                    .amount(debitRequest.getAmount())
                    .build();

            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName()+" "+foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        }
        }

    @Override
    public BankResponseDTO transferAmount(TransferRequest transferRequest) {
        String senderAcc = transferRequest.getSenderAccountNumber();
        String receiverAcc = transferRequest.getReceiverAccountNumber();

        Boolean senderAccountExists = userRepository.existsByAccountNumber(senderAcc);
        Boolean receiverAccountExists = userRepository.existsByAccountNumber(receiverAcc);

        if(!senderAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.SENDER_ACCOUNT_INVALID)
                    .responseMsg(AccountUtils.SENDER_ACCOUNT_INVALID_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        if(!receiverAccountExists){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.RECEIVER_ACCOUNT_INVALID)
                    .responseMsg(AccountUtils.RECEIVER_ACCOUNT_INVALID_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sender = userRepository.findByAccountNumber(senderAcc);
        if(sender.getAccountBalance().compareTo(transferRequest.getAmount())<0){
            return BankResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_FAILURE_CODE)
                    .responseMsg(AccountUtils.ACCOUNT_DEBITED_FAILURE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(sender.getFirstName()+" "+sender.getLastName())
                            .accountNumber(sender.getAccountNumber())
                            .accountBalance(sender.getAccountBalance())
                            .build())
                    .build();
        }

        User receiver = userRepository.findByAccountNumber(receiverAcc);

        sender.setAccountBalance(sender.getAccountBalance().subtract(transferRequest.getAmount()));
        receiver.setAccountBalance(receiver.getAccountBalance().add(transferRequest.getAmount()));

        userRepository.save(sender);
        TransactionDTO debitTransaction = TransactionDTO.builder()
                .transactionType("DEBIT")
                .accountNumber(sender.getAccountNumber())
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(debitTransaction);

        userRepository.save(receiver);
        TransactionDTO creditTransaction = TransactionDTO.builder()
                .transactionType("CREDIT")
                .accountNumber(receiver.getAccountNumber())
                .amount(transferRequest.getAmount())
                .build();
        transactionService.saveTransaction(creditTransaction);

        return BankResponseDTO.builder()
                .responseCode(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseMsg(AccountUtils.TRANSFER_SUCCESS_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountName( sender.getFirstName()+" "+sender.getLastName())
                        .accountNumber(sender.getAccountNumber())
                        .accountBalance(sender.getAccountBalance())
                        .build())
                .build();




    }
}

    //balanceEnquiry, credit, debit, transfer, nameEnquiry






