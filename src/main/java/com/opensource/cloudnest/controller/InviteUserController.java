package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class InviteUserController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/invite/user")
    @PreAuthorize("hasRole('ADMIN')")
    public String inviteUser(@RequestParam String email, @RequestParam String name) {
        return invitationService.inviteUser(email, name);
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerUser(@RequestParam String token, @RequestParam String password) {
        return invitationService.completeRegistration(token, password);
    }
}
