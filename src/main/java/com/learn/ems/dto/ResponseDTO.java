package com.learn.ems.dto;

public record ResponseDTO<T>(String message, Boolean success, T data) {}

