package ru.itis.jwtexample.services;

import org.springframework.security.core.Authentication;
import ru.itis.jwtexample.forms.SignUpForm;
import ru.itis.jwtexample.models.User;

import java.util.Optional;

public interface UserService {
    void signUp(SignUpForm signUpForm);
    Optional<User> findByLogin(String login);

}
