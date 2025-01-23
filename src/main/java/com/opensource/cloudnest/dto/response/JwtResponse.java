package com.opensource.cloudnest.dto.response;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class JwtResponse {
    private String token;
    private String name;
    private String email;
    private String status;
    private Integer profileId;
    private Long roleId;
    private String roleName;
    private long createdOn;
    private long updatedOn;
    private List<MenuObjectResponse> menuResponse;
    public JwtResponse(String token) {
        this.token = token;
    }
}