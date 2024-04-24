package com.bootakhae.userservice.services;

public interface EmailService {
    void sendMessage(String to);
    boolean verifyCode(String email, String code);
}
