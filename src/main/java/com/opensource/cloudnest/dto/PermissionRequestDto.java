package com.opensource.cloudnest.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class PermissionRequestDto {
    private String name;

    List<HashMap<String, Object>> subMenu ;

}