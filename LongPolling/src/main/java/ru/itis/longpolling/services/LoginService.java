package ru.itis.longpolling.services;

import ru.itis.longpolling.dto.TokenDto;
import ru.itis.longpolling.forms.LoginForm;

public interface LoginService {
    TokenDto login(LoginForm loginForm);
}
