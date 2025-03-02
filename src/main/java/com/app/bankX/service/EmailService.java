package com.app.bankX.service;

import com.app.bankX.dto.EmailDetails;

public interface EmailService {

    public void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
