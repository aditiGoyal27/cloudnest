package com.opensource.cloudnest.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name ="tbl_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String permissionName;
}
