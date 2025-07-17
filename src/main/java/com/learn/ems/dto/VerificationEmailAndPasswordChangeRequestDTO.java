package com.learn.ems.dto;

public record VerificationEmailAndPasswordChangeRequestDTO(String email, String newPassword, String verificationCode) {
}
