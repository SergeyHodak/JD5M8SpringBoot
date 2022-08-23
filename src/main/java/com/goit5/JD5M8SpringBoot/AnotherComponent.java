package com.goit5.JD5M8SpringBoot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class AnotherComponent {
    private final ChildComponent childComponent;

    @PostConstruct
    public void init() {
        childComponent.hello();
    }
}