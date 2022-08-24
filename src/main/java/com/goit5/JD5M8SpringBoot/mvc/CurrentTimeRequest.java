package com.goit5.JD5M8SpringBoot.mvc;

import lombok.Data;

@Data
public class CurrentTimeRequest {
    private String timezone;
    private String format;
}