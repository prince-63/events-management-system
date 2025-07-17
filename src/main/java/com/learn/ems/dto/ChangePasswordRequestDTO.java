package com.learn.ems.dto;

public record ChangePasswordRequestDTO(String currentPassword, String newPassword) {
}
