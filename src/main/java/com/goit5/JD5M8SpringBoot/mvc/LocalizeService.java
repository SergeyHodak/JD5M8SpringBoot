package com.goit5.JD5M8SpringBoot.mvc;

import org.springframework.stereotype.Service;

@Service
public class LocalizeService {
    public String getCurrentTimeLabel(String language) {
        if ("uk".equals(language)) {
            return "Поточний час";
        }

        return "Current time";
    }
}