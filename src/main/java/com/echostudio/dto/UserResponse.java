package com.echostudio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    public enum Role {
        USER,
        ADMIN
    }

    private String id, email;
    private Role role;
}
