package com.app.bankX.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "You already have an account";
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Your account creation is successful";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account does not exist. Please create an account";
    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "004";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "Account is credited successfully";
    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "Account is debited successfully";
    public static final String ACCOUNT_DEBITED_FAILURE_CODE = "006";
    public static final String ACCOUNT_DEBITED_FAILURE_MESSAGE= "Insufficient balance";
    public static final String SENDER_ACCOUNT_INVALID="007";
    public static final String SENDER_ACCOUNT_INVALID_MESSAGE="Sender Account does not exist";
    public static final String RECEIVER_ACCOUNT_INVALID="008";
    public static final String RECEIVER_ACCOUNT_INVALID_MESSAGE="Receiver Account does not exist";
    public static final String TRANSFER_SUCCESS_CODE="009";
    public static final String TRANSFER_SUCCESS_MSG="Fund Transfer is successfully completed";

    public static String generateAccountNumber(){
        int min = 100000;
        int max = 999999;

        Year currYear = Year.now();
        int randNumber = (int) Math.floor(Math.random() * (max-min + 1) + min);

        return String.valueOf(currYear) + String.valueOf(randNumber);
    }

}
