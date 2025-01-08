package com.opensource.cloudnest.dto;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Data
public class PermissionDTO {
    private Map<Long, List<Long>> rolePermissionsMap; // Key: Role ID, Value: List of Permission IDs

}
