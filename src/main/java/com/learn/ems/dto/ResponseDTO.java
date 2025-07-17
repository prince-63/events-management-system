package com.learn.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class ResponseDTO <T> {
    public String message;
    public Boolean success;
    public T data;
}

