package com.goit5.JD5M8SpringBoot.feature.name;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class NameController {
//    @Value(value = "${appName}") //спринг експрешин ленгвич.. Будешукатись з змінних оточення або з налаштувань пропертіс
//    private String appName;

    @GetMapping("/name")
    public String getName() {
        String appName = "test/path";
        return appName;
        //адреса запиту: localhost:8080/api/v1/name
    }
}