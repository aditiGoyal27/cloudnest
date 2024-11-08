package com.opensource.cloudnest.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.List;



@Data
@Entity
@Table(name = "tbl_tenant")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orgName;
    private String orgUnitName;
    private String orgLocation;
    private String orgEmail;
    private String orgContactNumber;
    private String status;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "admin_id") // Explicitly define join column for the admin
    private Profile admin;

    @OneToMany(mappedBy = "tenant") // Set the mappedBy attribute to define ownership
    private List<Profile> users;

    @OneToOne
    @JoinColumn(name = "billing_details_id") // Explicitly define join column for billing details
    private BillingDetails billingDetails;
}
