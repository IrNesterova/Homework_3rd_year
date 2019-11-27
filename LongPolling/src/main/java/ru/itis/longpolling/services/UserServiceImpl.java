package ru.itis.longpolling.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.longpolling.forms.SignUpForm;
import ru.itis.longpolling.models.Role;
import ru.itis.longpolling.models.User;
import ru.itis.longpolling.repositories.UsersRepository;
import ru.itis.longpolling.security.details.UserDetailsImpl;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignUpForm signUpForm) {
        String hashPassword = passwordEncoder.encode(signUpForm.getPassword());
        User user = User.builder()
                .login(signUpForm.getLogin())
                .passwordHash(hashPassword)
                .role(Role.USER)
                .tokens(new ArrayList<>())
                .build();
        usersRepository.save(user);
    }

    @Override
    public User getCurrentUser(Authentication authentication) {
        return ((UserDetailsImpl)authentication.getPrincipal()).getUser();
    }
}
