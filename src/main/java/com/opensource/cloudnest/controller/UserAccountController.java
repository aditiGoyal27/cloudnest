package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile/user")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create/{adminId}")
    public ResponseEntity<ResDTO<Object>> createUser(@RequestBody  SignUpDTO signUpDTO , @PathVariable Long adminId) {
        return new ResponseEntity<>(userAccountService.createUser(signUpDTO , adminId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update/{adminId}")
    public ResponseEntity<ResDTO<Object>> updateUser(@RequestBody  SignUpDTO signUpDTO , @PathVariable Long adminId) {
        return new ResponseEntity<>(userAccountService.updateUser(signUpDTO , adminId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{adminId}/{userId}")
    public ResponseEntity<ResDTO<Object>> deleteUser( @PathVariable Long adminId , @PathVariable Long userId) {
        return new ResponseEntity<>(userAccountService.deleteUser(adminId , userId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/suspend/{adminId}/{userId}")
    public ResponseEntity<ResDTO<Object>> suspendUser( @PathVariable Long adminId , @PathVariable Long userId) {
        return new ResponseEntity<>(userAccountService.suspendUser(adminId , userId), HttpStatus.OK);
    }
}
