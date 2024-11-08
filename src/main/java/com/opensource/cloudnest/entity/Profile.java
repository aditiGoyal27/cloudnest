package com.opensource.cloudnest.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tbl_profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String userName;
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String contactNumber;
    private String orgName;
    @OneToOne
    @JoinColumn(name = "role_id" , referencedColumnName = "id")
    private Role role;
    private String status;
    private boolean enabled = false;
    @ManyToMany
    private List<Relation> relation;
    @ManyToOne
    @JoinColumn(name = "tenant_id") // Define the foreign key column in Profile table
    private Tenant tenant;

}
