package com.learn.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ResponseDTO <T> {
    public String message;
    public Boolean success;
    public T data;
}

