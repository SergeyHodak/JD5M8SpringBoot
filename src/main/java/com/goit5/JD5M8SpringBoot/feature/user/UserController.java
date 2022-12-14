package com.goit5.JD5M8SpringBoot.feature.user;

import com.goit5.JD5M8SpringBoot.feature.user.dto.DeleteUserResponse;
import com.goit5.JD5M8SpringBoot.feature.user.dto.SaveUserResponse;
import com.goit5.JD5M8SpringBoot.feature.user.dto.UserDTO;
import com.goit5.JD5M8SpringBoot.feature.user.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class UserController {
    private final UserService userService;
    private final UserValidateService validateService;

    @GetMapping("/list")
    public List<UserDTO> list() {
        return userService.findAll().stream().map(UserDTO::fromUser).collect(Collectors.toList());
        //Посилання запиту: localhost:8080/api/v1/user/list
    }

    @PostMapping("/save")
    public SaveUserResponse save(@RequestBody UserDTO userDTO) {
        if (!validateService.isEmailValid(userDTO.getEmail())) {
            return SaveUserResponse.failed(SaveUserResponse.Error.invalidEmail);
        }

        User user = UserDTO.fromDto(userDTO);

        userService.save(user);

        return SaveUserResponse.success();
        //Посилання запиту: localhost:8080/api/v1/user/save
        //Тіло запиту:
        //{
        //    "email": "john.doe@mail.com",
        //    "fullName": "John Doe",
        //    "birthday": "2000-01-02",
        //    "gender": "male"
        //}
    }

    @PostMapping("/delete/{email}")
    public Object delete(@PathVariable("email") String email, HttpServletResponse response) {
        if (!userService.exists(email)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
        }

        userService.deleteById(email);

        return DeleteUserResponse.success();
        //Посилання запиту: localhost:8080/api/v1/user/delete/john.doe@mail.com
    }

    @PostMapping("/deleteAll")
    public DeleteUserResponse deleteAll(@RequestBody List<String> emails) {
        boolean thereAreNonExistingEmails = emails == null || emails.stream().allMatch(email -> !userService.exists(email));
        if (thereAreNonExistingEmails) {
            return DeleteUserResponse.failed(DeleteUserResponse.Error.userNotFound);
        }

        userService.deleteByIds(emails);

        return DeleteUserResponse.success();

        //Посилання запиту: localhost:8080/api/v1/user/deleteAll
        //Тіло запиту: JSON
        //[
        //    "john.doe@mail.com",
        //    "john.doe1@mail.com"
        //]
    }

    @GetMapping("/search")
    public List<UserDTO> search(@RequestParam(name = "query", required = false) String query,
                                HttpServletResponse response) {
        if (!validateService.isSearchQueryValid(query)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Collections.emptyList();
        }

        return userService.search(query).stream().map(UserDTO::fromUser).collect(Collectors.toList());
        //Посилання запиту: localhost:8080/api/v1/user/search
        //Параметр: key=query value=John Doe
    }

    @GetMapping("/countOlderThen/{age}")
    public int countPeopleOlderThan(@PathVariable("age") int age) {
        return userService.countPeopleOlderThan(age);
        //Посилання запиту: localhost:8080/api/v1/user/countOlderThen/30
    }

    @GetMapping("/info/{email}")
    public UserInfo getUserInfo(@PathVariable("email") String email) {
        //return userService.getUserInfo(email);
        return userService.getUserInfoV2(email);

        //Посилання запиту: localhost:8080/api/v1/user/info/john.doe1@mail.com
    }

    @GetMapping("/between")
    public List<User> getUsersBetween(@RequestParam("start") String start,
                                      @RequestParam("end") String end) {
        //складний предикат
        return userService.getUsersBetween(
                LocalDate.parse(start),
                LocalDate.parse(end)
        );

        //Посилання запиту: localhost:8080/api/v1/user/between
        //Параметри:
        // "key=start": "value:1990-01-01"
        // "key=end": "value:2022-01-01"
    }
}