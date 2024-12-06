package com.opensource.cloudnest.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String name;
    private String email;
    private String status;
    private Integer profileId;
    public JwtResponse(String token) {
        this.token = token;
    }
}