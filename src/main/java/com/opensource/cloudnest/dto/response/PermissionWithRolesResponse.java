package com.opensource.cloudnest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PermissionWithRolesResponse {
    private String roleNames; // List of role names associated with this permission
    private Long roleId;
    private PermissionResponse permissionResponse;

}
