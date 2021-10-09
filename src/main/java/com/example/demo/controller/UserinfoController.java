package com.example.demo.controller;

import com.example.demo.model.VehicleAppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController()
public class UserinfoController {

    @GetMapping("/me")
    public String currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();

        return username + "\n" + roles;

        // This was provided, does not work..
        // VehicleAppUser user = (VehicleAppUser) authentication.getPrincipal();
        // return user.getUsername() + "\n" + user.getRoles();
    }

    // from: https://www.baeldung.com/get-user-in-spring-security
    // @RequestMapping(value = "/username", method = RequestMethod.GET)
    @GetMapping("/username")
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        String username = authentication.getName();
        String roles = authentication.getAuthorities().toString();
        // return username + "\n" + roles;

        // Above works as well, below does not
        Object principal = authentication.getPrincipal();
        System.out.println("only for debug");
        VehicleAppUser user = (VehicleAppUser) authentication.getPrincipal();
        return user.getUsername() + "\n" + user.getRoles();
    }

}
