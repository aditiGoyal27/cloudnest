package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.SignUpDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "https://app.wavematrix.ai/"})
@RestController
@RequestMapping("/profile/user")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    ProfileRepository profileRepository;
    @PreAuthorize("hasRole('ROLE_TENANT_ADMIN') OR hasPermission(authentication.principal.id, 'CREATE_USER')")
    @PostMapping("/create")
    public ResDTO<Object> createUser(HttpServletRequest request , @RequestBody  SignUpDTO signUpDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.createUser(signUpDTO , jwtTokenProvider.getProfileFromRequest(request)));
        }
        return new  ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE ,"Invalid Data");
    }

    @PreAuthorize("hasRole('ROLE_TENANT_ADMIN') OR hasPermission(authentication.principal.id, 'UPDATE_USER')")
    @PostMapping("/update")
    public ResDTO<Object> updateUser(HttpServletRequest request ,@RequestBody  SignUpDTO signUpDTO) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.updateUser(signUpDTO , jwtTokenProvider.getProfileFromRequest(request)));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE ,"Invalid data");
    }

    @PreAuthorize("hasRole('ROLE_TENANT_ADMIN') OR hasPermission(authentication.principal.id, 'DELETE_USER')")
    @DeleteMapping("/delete")
    public ResDTO<Object> deleteUser(HttpServletRequest request , @RequestParam Integer userId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.deleteUser(jwtTokenProvider.getProfileFromRequest(request) , userId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid Data" );
    }


    @PostMapping("/suspend")
    public ResDTO<Object> suspendUser(HttpServletRequest request,  @RequestParam Integer userId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.suspendUser(jwtTokenProvider.getProfileFromRequest(request) , userId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid Data" );
    }


    @PostMapping("/reactivate")
    public ResDTO<Object> reactivateUser(HttpServletRequest request,@RequestParam Integer userId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,userAccountService.reactivateUser(jwtTokenProvider.getProfileFromRequest(request) , userId));
        }
        return new ResDTO<>(Boolean.FALSE, ResDTOMessage.FAILURE, "Invalid Data" );
    }

}
