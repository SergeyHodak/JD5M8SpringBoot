package com.goit5.JD5M8SpringBoot.feature.time.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
    @PostConstruct
    public void init() {
        //CREATE & UPDATE
        userRepository.save(new User());

        //READ
        userRepository.findAll();
        userRepository.findById("");

        //DELETE
        userRepository.delete(new User());
        userRepository.deleteById("");

        System.out.println(userRepository.getClass()); //class com.sun.proxy.$Proxy101
    }
    */

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exists(String email) {
        if (email == null) {
            return false;
        }

        return userRepository.existsById(email);
    }

    public void deleteById(String email) {
        userRepository.deleteById(email);
    }

    public List<User> search(String query)  {
        //return userRepository.search("%" + query + "%");
        //return userRepository.searchByNativeSqlQuery("%" + query + "%");
        return userRepository.findAllById(
                userRepository.searchEmails("%" + query + "%")
        );
    }

    public int countPeopleOlderThan(int age) {
        //return (int) findAll().stream()
        //        .filter(u -> {
        //            int userAge = (int) ChronoUnit.YEARS.between(u.getBirthday(), LocalDate.now());
        //            return userAge > age;
        //        })
        //        .count();

        LocalDate maxBirthday = LocalDate.now().minusYears(age + 1);
        return userRepository.countOlderThan(maxBirthday);
    }
}