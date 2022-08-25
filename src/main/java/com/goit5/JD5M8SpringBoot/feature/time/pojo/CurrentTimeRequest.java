package com.goit5.JD5M8SpringBoot.feature.time.pojo;

import lombok.Data;

@Data
public class CurrentTimeRequest {
    private String timezone;
    private String format;
}