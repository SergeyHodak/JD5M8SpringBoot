package com.goit5.JD5M8SpringBoot.feature.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public boolean hasAuthority(String name) {
//        for (GrantedAuthority authority : getUser().getAuthorities()) {
//            if (name.equals(authority.getAuthority())) {
//                return true;
//            }
//        }
//
//        return false;

        return getUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(it -> it.equals(name));
    }

    public String getUsername() {
        return getUser().getUsername();
    }

    private User getUser() {
        return (User) getAuthentication().getPrincipal();
    }

    private Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();

        return context.getAuthentication();
    }
}