package ru.itis.jwtexample.forms;

import lombok.Data;

@Data
public class SignUpForm {
    private String login;
    private String password;
}
