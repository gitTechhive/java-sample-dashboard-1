package com.sampledashboard1.service;

import com.sampledashboard1.payload.request.MailRequest;

public interface EmailService {
    String sendEmail(MailRequest request);
}
