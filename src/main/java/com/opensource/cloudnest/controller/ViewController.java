package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.repository.ProfileRepository;
import com.opensource.cloudnest.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/app/view")
public class ViewController {

    @Autowired
    private ViewService viewService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    ProfileRepository profileRepository;
    @PreAuthorize("hasRole('ADMIN') OR hasPermission(#adminId, 'VIEW_PROFILE')")
    @GetMapping("/profile")
    public ResDTO<Object> viewProfile(HttpServletRequest request ,@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size
                                                      ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,viewService.viewProfile(jwtTokenProvider.getProfileFromRequest(request) , page , size));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filterProfile")
    public ResDTO<Object> filterProfile(HttpServletRequest request ,@RequestParam String name,
                                      @RequestParam String  email, @RequestParam String phoneNumber
                                      ) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request , profileRepository)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,viewService.filterProfile( name , email , phoneNumber , jwtTokenProvider.getProfileFromRequest(request) ));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

}
