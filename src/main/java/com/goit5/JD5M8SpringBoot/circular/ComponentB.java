package com.goit5.JD5M8SpringBoot.circular;

//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

//import javax.annotation.PostConstruct;

//@DependsOn("componentA") //створи екземпляр цього класу після того як створиш екземпляр класу вказаного в дужках
@Service
public class ComponentB {
    //private final ComponentA componentA;

//    private final ApplicationContext context;
//
//    public ComponentB(ApplicationContext context) {
//        this.context = context;
//        System.out.println("Creating B");
//    }
//
//    @PostConstruct
//    public void init() {
//        getComponentA().print();
//    }
//
//    private ComponentA getComponentA() {
//        return context
//                .getBeansOfType(ComponentA.class)
//                .values()
//                .stream()
//                .findFirst()
//                .orElse(null);
//    }
}