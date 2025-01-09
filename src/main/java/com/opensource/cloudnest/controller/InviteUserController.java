package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = { "http://mdmdev.elements91.in", "http://10.10.2.12:3000", "http://10.10.2.21:3000", "http://localhost:3000", "https://mdmdev.elements91.in"})
@RestController
@RequestMapping("/app")
public class InviteUserController {

    @Autowired
    private InvitationService invitationService;

    @PostMapping("/invite/user/{adminId}")
    @PreAuthorize("hasRole('ADMIN') and hasPermission(#adminId, 'INVITE_USER')")
    public String inviteUser(@RequestParam String email, @RequestParam String name) {
        return invitationService.inviteUser(email, name);
    }

    @PostMapping("/register/user/{adminId}")
    @PreAuthorize("hasRole('ADMIN') AND hasPermission(#adminId, 'REGISTER_USER')")
    public String registerUser(@RequestParam String token, @RequestParam String password) {
        return invitationService.completeRegistration(token, password);
    }
}
