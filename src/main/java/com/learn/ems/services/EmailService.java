package com.learn.ems.services;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
