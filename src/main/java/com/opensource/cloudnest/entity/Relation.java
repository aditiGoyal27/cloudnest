package com.opensource.cloudnest.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name ="tbl_relation")
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "admin_id")
    private Profile adminId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Profile userId;

}
