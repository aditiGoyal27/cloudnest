package com.opensource.cloudnest.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    @Column(unique = true)
    private String email;
    private String contactNumber;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private String status;
    private boolean enabled = true;
    @ManyToMany
    private List<Relation> relation;
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
    @ManyToOne
    @JoinColumn(name = "super_admin_id")
    private Profile superAdmin;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Profile admin;
    private long createdOn;
    private long updatedOn;
}
