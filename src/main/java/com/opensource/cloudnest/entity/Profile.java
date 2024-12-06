package com.opensource.cloudnest.entity;


import jakarta.persistence.*;
import lombok.*;
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
    private boolean enabled = false;
    @ManyToMany
    private List<Relation> relation;
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

}
