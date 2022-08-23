package com.goit5.JD5M8SpringBoot;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@Component
public class TestComponent {
    public TestComponent() {
        System.out.println("In constructor");
    }

    @PostConstruct
    public void init1() {
        System.out.println("In PostConstructor");
    }

    @PreDestroy
    public void onDestroy() {
        System.out.println("In onDestroy");
    }
}