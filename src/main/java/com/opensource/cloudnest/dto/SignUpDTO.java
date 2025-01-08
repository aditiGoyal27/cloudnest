package com.opensource.cloudnest.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignUpDTO {

    private String name;
    private String email;
    private String password;
    private String contactNumber;
    private List<String> roles;
    }
