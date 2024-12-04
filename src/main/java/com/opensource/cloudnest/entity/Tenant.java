package com.opensource.cloudnest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;



@Data
@Entity
@Table(name = "tbl_tenant")
@EntityListeners(AuditingEntityListener.class)
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
    private Profile admin;

    @OneToMany(mappedBy = "tenant") // Set the mappedBy attribute to define ownership
    private List<Profile> users;

    @OneToOne
    private BillingDetails billingDetails;
}
