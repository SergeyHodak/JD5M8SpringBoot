package com.goit5.JD5M8SpringBoot.feature.auth;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
//    private static final Map<String, String> USERS = Map.of(
//            "user", "user",
//            "admin", "admin"
//    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDate userDate = getByIdOrNull(username);
//        if (!USERS.containsKey(username)) {
//            throw new UsernameNotFoundException(username);
//        }
        if (userDate == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
//                return Collections.singleton((GrantedAuthority) () -> username);

//                return Collections.singleton((GrantedAuthority) userDate::getAuthority);

                return Arrays.stream(userDate.getAuthority().split(","))
                        .map(it -> (GrantedAuthority) () -> it)
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
//                return USERS.get(username);
                return userDate.getPassword();
            }

            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public boolean isAccountNonExpired() {
                return true; //чи темін дії аккаунта ще не вийшов
            }

            @Override
            public boolean isAccountNonLocked() {
                return true; //чи аккаунт не заблокований
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    private UserDate getByIdOrNull(String email) {
        String sql = "SELECT password, authority FROM \"user\" WHERE email = :email";
        return jdbcTemplate.queryForObject(
                sql,
                Map.of("email", email),
                //UserDate.class
                new UserDataMapper()
        );
    }

    private static class UserDataMapper implements RowMapper<UserDate> {
        @Override
        public UserDate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserDate.builder()
                    .password(rs.getString("password"))
                    .authority(rs.getString("authority"))
                    .build();
        }
    }

    @Builder
    @Data
    private static class UserDate {
        private String password;
        private String authority;
    }

    //TODO stopped 19:20 in lecture 30
}