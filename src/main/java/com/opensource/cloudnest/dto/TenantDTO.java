package com.opensource.cloudnest.dto;

import lombok.Data;

@Data
public class TenantDTO {
    private String tenantName;
    private String orgName;
    private String orgAdminName;
    private String orgAdminEmail;

}
