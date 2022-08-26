package com.goit5.JD5M8SpringBoot.feature.user.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private UserDTO user;
    private List<String> addresses;
}