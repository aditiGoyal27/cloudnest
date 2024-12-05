package com.opensource.cloudnest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name ="tbl_permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String permissionName;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
