package com.goit5.JD5M8SpringBoot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor // шукає фінал поля, та генерує для них конструктор
@Component
public class ParentComponent {
//    @Autowired //дай нам такий то компонент (для полів цю аннотацію використовувати не варто)
//    private final ChildComponent childComponent;

//    public ParentComponent(ChildComponent childComponent) {
//        this.childComponent = childComponent;
//    }

//    @Autowired
//    public void setChildComponent(ChildComponent childComponent) {
//        this.childComponent = childComponent;
//    }

    private final ApplicationContext context;

    @PostConstruct
    public void init() {
//        ChildComponent childComponent = context.getBean("iChildComponent", ChildComponent.class);
//        childComponent.hello();
        for (ChildComponent component : context.getBeansOfType(ChildComponent.class).values()) {
            component.hello();
        }
    }
}