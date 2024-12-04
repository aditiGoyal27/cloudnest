package com.opensource.cloudnest.dto;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class PermissionDTO {
    private String permissionName;
    private List<String> role;
}
