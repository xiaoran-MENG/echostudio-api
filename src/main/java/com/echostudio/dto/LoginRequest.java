package com.echostudio.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email, password;
}
