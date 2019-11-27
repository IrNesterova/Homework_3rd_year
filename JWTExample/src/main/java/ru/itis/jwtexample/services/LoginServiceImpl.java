package ru.itis.jwtexample.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.jwtexample.dto.TokenDto;
import ru.itis.jwtexample.forms.LoginForm;
import ru.itis.jwtexample.models.User;

import java.util.Optional;

import static ru.itis.jwtexample.dto.TokenDto.from;

@Service
public class LoginServiceImpl implements LoginService {

    private final static String PREFIX = "Bearer ";

    @Autowired
    private UserService userService;

    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    @Value("${jwt.secret}")
    private String key;


    @Override
    public String createToken(User user) {
        return Jwts.builder()
                .claim("login", user.getLogin())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }


    @Override
    public TokenDto login(LoginForm loginForm) {
        Optional<User> userCandidate = userService.findByLogin(loginForm.getLogin());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            return from(createToken(user));
        }
        throw new IllegalArgumentException("Can not find such user");
    }
}
