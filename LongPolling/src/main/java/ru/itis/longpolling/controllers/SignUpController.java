package ru.itis.longpolling.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.longpolling.forms.SignUpForm;
import ru.itis.longpolling.services.UserService;


@RestController
public class SignUpController {

    @Autowired
    private UserService userService;

    @CrossOrigin
    @PostMapping("/signUp")
    @PreAuthorize("permitAll()")
    public void signUpUser(@RequestBody SignUpForm signUpForm) {
        userService.signUp(signUpForm);
    }
}
