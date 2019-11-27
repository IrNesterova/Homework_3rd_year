package ru.itis.longpolling.services;

import org.springframework.security.core.Authentication;
import ru.itis.longpolling.forms.SignUpForm;
import ru.itis.longpolling.models.User;


public interface UserService {
    void signUp(SignUpForm signUpForm);

    User getCurrentUser(Authentication authentication);
}
