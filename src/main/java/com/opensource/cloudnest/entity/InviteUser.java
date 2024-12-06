package com.opensource.cloudnest.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Data
@Entity
@Table(name = "tbl_invite_user")
public class InviteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String email;
    private String name;
    private String password;
    private boolean enabled;
    private String inviteToken;
    private LocalDateTime inviteTokenExpiry;
}
