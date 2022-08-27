package com.goit5.JD5M8SpringBoot.feature.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.userdetails.User;

//ЩОБ розлогінитись потрібно перейти на запит: localhost:8080/logout

@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthController {
    private final AuthService authService;

    @GetMapping("/profile")
    public ModelAndView get() {
        ModelAndView result = new ModelAndView("auth-page");

//        SecurityContext context = SecurityContextHolder.getContext();
//        System.out.println("SecurityContextHolder.getContextHolderStrategy().getClass() = " + SecurityContextHolder.getContextHolderStrategy().getClass()); //за якою стратегією працює
//        Authentication authentication = context.getAuthentication();
//        String username = authentication.getName();
        //result.addObject("username", username);
        result.addObject("username", authService.getUsername());

//        User principal = (User) authentication.getPrincipal();
//        //System.out.println("principal.getClass() = " + principal.getClass());
//        System.out.println("principal.getUsername() = " + principal.getUsername()); // імя користувача (логін)
//        System.out.println("principal.getAuthorities() = " + principal.getAuthorities()); //які права у даного користувача є
//
//        System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId()); //В якому потоці

        return result;

        //Адреса звернення: localhost:8080/auth/profile
        //login: user
        //password: шукати в логах запуску программи
    }

    @GetMapping("/superadmin")
    public ModelAndView superAdminOnly() {
        if (!authService.hasAuthority("admin")) {
            return new ModelAndView("forbidden");
        }

        return new ModelAndView("superadmin");
        //Адреса звернення: localhost:8080/auth/superadmin
    }
}