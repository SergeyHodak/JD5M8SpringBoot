package com.goit5.JD5M8SpringBoot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Jd5M8SpringBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(Jd5M8SpringBootApplication.class, args);

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(30);
//        System.out.println("passwordEncoder.encode(\"user_password\") = " + passwordEncoder.encode("user_password")); // $2a$10$ICi9NDXUPSrxAgBdATqSK.8xHS.D4SWMK9OsuwW7CPj0qxCWLBhAC
//        System.out.println("passwordEncoder.encode(\"admin_password\") = " + passwordEncoder.encode("admin_password")); // $2a$10$epG9qWLVKfZe/G8wY6j6kO4s7ZbTkgW.TLUgaKNqDBYInzopjeIx2
    }
}