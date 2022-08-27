package com.goit5.JD5M8SpringBoot.feature.auth;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.authenticator.SpnegoAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
public class DefaultSecurityConfig {
    private final CustomAuthProvider authProvider;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //запит для гугляжу: ant matchers
        http
                //типу все що по вказаній адресі доступно без авторизації

                .authorizeRequests()
                .antMatchers("/api/v1/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()

//                //щоб все було дозволино
//                //будь який запит повинен бути аунтетифікований
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                .and()

                .httpBasic()
                .and()

//                .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/auth").hasAnyRole() //може зайти юзер з бедь якою ролью на таке посилання за методом гет
//                .antMatchers(HttpMethod.GET, "/non-auth").permitAll() //дозволяєм всім
//                .and()
                .formLogin(Customizer.withDefaults());
        return http.build();
    }

    @Autowired
    public void injectCustomAuthProvider(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }
}