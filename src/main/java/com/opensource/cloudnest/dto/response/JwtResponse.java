package com.opensource.cloudnest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String name;
    private String email;
    private String status;
    private Integer profileId;
    private Long roleId;
    private String roleName;
    private List<PermissionResponse> permissionResponse;
    public JwtResponse(String token) {
        this.token = token;
    }
}