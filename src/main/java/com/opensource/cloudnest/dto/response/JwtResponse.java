package com.opensource.cloudnest.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String name;
    private String contactNumber;
    private String email;
    private String orgName;
    private String status;
    public JwtResponse(String token) {
        this.token = token;
    }
}