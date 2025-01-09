package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.configuration.JwtTokenProvider;
import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.dto.response.ResDTOMessage;
import com.opensource.cloudnest.service.ViewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = { "http://mdmdev.elements91.in", "http://10.10.2.12:3000", "http://10.10.2.21:3000", "http://localhost:3000", "https://mdmdev.elements91.in"})
@RestController
@RequestMapping("/app/view")
public class ViewController {

    @Autowired
    private ViewService viewService;

    @PreAuthorize("hasRole('ADMIN') and hasPermission(#adminId, 'VIEW_PROFILE')")
    @GetMapping("/profile/{adminId}")
    public ResDTO<Object> viewProfile(HttpServletRequest request ,@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @PathVariable Integer adminId) {
        if (JwtTokenProvider.validateProfileIdInAccessToken(request, adminId)) {
            return new ResDTO<>(Boolean.TRUE, ResDTOMessage.SUCCESS ,viewService.viewProfile(adminId , page , size));
        }
        return new ResDTO<>(Boolean.FALSE , ResDTOMessage.FAILURE , "Invalid Data");

    }

}
