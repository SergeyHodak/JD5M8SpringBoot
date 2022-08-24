package com.goit5.JD5M8SpringBoot.circular;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ComponentA {
//    private final ApplicationContext context;

//    public ComponentA(ApplicationContext context) {
//        this.context = context;
//        System.out.println("Creating A");
//    }

    public void print() {
        System.out.println("I am componentA start");
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("I am componentA end");
    }

//    private ComponentB getComponentB() {
//        return context
//                .getBeansOfType(ComponentB.class)
//                .values()
//                .stream()
//                .findFirst()
//                .orElse(null);
//    }
}