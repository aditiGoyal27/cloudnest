package com.opensource.cloudnest.controller;

import com.opensource.cloudnest.dto.ResDTO;
import com.opensource.cloudnest.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/view")
public class ViewController {

    @Autowired
    private ViewService viewService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/profile/{adminId}")
    public ResponseEntity<ResDTO<Object>> viewProfile(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @PathVariable Long adminId) {
        return new ResponseEntity<>(viewService.viewProfile(adminId , page , size), HttpStatus.OK);

    }

}
