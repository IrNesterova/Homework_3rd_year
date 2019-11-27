package ru.itis.jwtexample.services;

import ru.itis.jwtexample.dto.TokenDto;
import ru.itis.jwtexample.forms.LoginForm;
import ru.itis.jwtexample.models.User;

public interface LoginService {
    String createToken(User user);

    TokenDto login(LoginForm loginForm);
}
