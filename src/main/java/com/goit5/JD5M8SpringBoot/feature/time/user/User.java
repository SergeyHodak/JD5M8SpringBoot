package com.goit5.JD5M8SpringBoot.feature.time.user;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "\"user\"")
@Data
@Entity
public class User {
    @Id
    private String email;

    private String fullName;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING) //Потрібно зберігати в базі данних рядками
    private Gender gender;
}