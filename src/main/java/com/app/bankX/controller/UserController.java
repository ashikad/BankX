package com.app.bankX.controller;

import com.app.bankX.dto.*;
import com.app.bankX.entity.User;
import com.app.bankX.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user and assigning an account ID"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping
    public BankResponseDTO createAccount(@RequestBody UserRequestDTO userRequest){
        return userService.createAccount(userRequest);
    }

    @Operation(
            summary = "Balance Enquiry",
            description = "Getting balance details for the given account number "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )
    @GetMapping("/balance")
    public BankResponseDTO balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.balancyEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Name Enquiry",
            description = "Getting name of the user for given the account number"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 200 SUCCESS"
    )

    @GetMapping("/name")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Credit amount",
            description = "Crediting the given amount to the given account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/credit")
    public BankResponseDTO creditAccount(@RequestBody CreditDebitRequest creditRequest){
        return userService.creditAccount(creditRequest);
    }

    @Operation(
            summary = "Debit amount",
            description = "Debiting the given amount from the given account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/debit")
    public BankResponseDTO debitAccount(@RequestBody CreditDebitRequest debitRequest){
        return userService.debitAccount(debitRequest);
    }

    @Operation(
            summary = "Transfer amount",
            description = "Transferring the given amount from sender account to the receiver account"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping("/transfer")
    public BankResponseDTO transferAmount(@RequestBody TransferRequest transferRequest){
        return userService.transferAmount(transferRequest);
    }
}
