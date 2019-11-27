package ru.itis.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.itis.dto.MessageDto;
import ru.itis.dto.TokenDto;
import ru.itis.forms.LoginForm;
import ru.itis.forms.UserForm;
import ru.itis.models.Message;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;

import java.util.Optional;

import static ru.itis.dto.TokenDto.from;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    @Value("${jwt.secret}")
    private String key;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    private String createToken(User user) {
        return Jwts.builder()
                .claim("login", user.getLogin())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

//    public TokenDto login(LoginForm loginForm) {
//        Optional<User> userCandidate = getUserByName(loginForm.getLogin());
//
//        if (userCandidate.isPresent()) {
//            User user = userCandidate.get();
//            return from(createToken(user));
//        }
//        throw new IllegalArgumentException("Can not find such user");
//    }

    public Optional<User> getUserByName(String userName) {
        if (userRepository.findByLogin(userName).isPresent()) {
            return userRepository.findByLogin(userName);
        }
        return Optional.empty();
    }

    public boolean login(MessageDto messageDto) {
        Optional<User> userCandidate = getUserByName(messageDto.getSender());
        if(userCandidate.isPresent()) {
            return userCandidate.filter(user -> checkPassword(messageDto.getPassword(), user)).isPresent();
        }
        return false;
    }

    private boolean checkPassword(String password, User userCandidate) {
        return userCandidate.getPassword().equals(password);
    }

    public User signUp(MessageDto messageDto) {
//        String hashPassword = passwordEncoder.encode(signUpForm.getPassword());
        User newUser = User.builder()
                .login(messageDto.getSender())
                .password(messageDto.getPassword())
                .build();
        return userRepository.save(newUser);
    }
}
