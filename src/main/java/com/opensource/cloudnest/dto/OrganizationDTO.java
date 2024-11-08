package com.opensource.cloudnest.dto;

import lombok.Data;

@Data
public class OrganizationDTO {
    private String orgName;
    private String orgUnitName;
    private String orgLocation;
    private String orgEmail;
    private String orgContactNumber;
    private String status;
}
