package com.opensource.cloudnest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PermissionWithRolesResponse {
    private String roleName; // List of role names associated with this permission
    private Long roleId;
    private List<String> permissionName;
    private List<Long> permissionId;

}
