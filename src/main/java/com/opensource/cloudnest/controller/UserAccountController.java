package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
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
    @PostMapping("/create")
    public ResDTO<Object> createUser(HttpServletRequest request , @RequestBody  SignUpDTO signUpDTO , @RequestParam Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.createUser(signUpDTO , adminId));
        }
        return new  ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE ,"Invalid Data");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public ResDTO<Object> updateUser(HttpServletRequest request ,@RequestBody  SignUpDTO signUpDTO , @RequestParam Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.updateUser(signUpDTO , adminId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE ,"Invalid data");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResDTO<Object> deleteUser(HttpServletRequest request , @RequestParam Integer adminId , @RequestParam Integer userId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.deleteUser(adminId , userId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid Data" );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/suspend")
    public ResDTO<Object> suspendUser(HttpServletRequest request, @RequestParam Integer adminId , @RequestParam Integer userId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.suspendUser(adminId , userId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid Data" );
    }
}
