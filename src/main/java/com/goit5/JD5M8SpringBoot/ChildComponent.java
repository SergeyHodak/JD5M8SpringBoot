package com.goit5.JD5M8SpringBoot;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("singleton") //@Scope("prototype") --якщо так то для кожного інжекта будестворюватись новий компонент
@Component
public class ChildComponent implements IChildComponent {
    public ChildComponent() {
        System.out.println("In ChildComponent Constructor");
    }

    @Override
    public void hello() {
        System.out.println("Hello, I'm ChildComponent");
    }
}