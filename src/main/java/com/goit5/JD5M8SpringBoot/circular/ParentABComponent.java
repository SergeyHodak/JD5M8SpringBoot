package com.goit5.JD5M8SpringBoot.circular;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class ParentABComponent {
    private final ComponentA componentA;
    private final ComponentB componentB;

    @PostConstruct
    public void init() {
        componentA.print();
    }
}