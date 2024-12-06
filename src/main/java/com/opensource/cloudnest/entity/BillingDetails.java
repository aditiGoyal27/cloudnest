package com.opensource.cloudnest.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_billing_details")
public class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Changed from String to Long

    private Long amount;

    @OneToOne // Assuming one billing detail is linked to one tenant
    @JoinColumn(name = "tenant_id") // Specify the foreign key column name in the database
    private Tenant tenant;

    @OneToOne
    @JoinColumn(name = "admin_id") // Specify the foreign key column name in the database
    private Profile admin;

}
