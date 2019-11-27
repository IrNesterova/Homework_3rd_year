package ru.itis.longpolling.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.longpolling.dto.TokenDto;
import ru.itis.longpolling.forms.LoginForm;
import ru.itis.longpolling.models.Token;
import ru.itis.longpolling.models.User;
import ru.itis.longpolling.repositories.TokensRepository;
import ru.itis.longpolling.repositories.UsersRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TokensRepository tokensRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsersRepository usersRepository;

    @Value("${token.expired}")
    private Integer expiredSecondsForToken;

    @Override
    public TokenDto login(LoginForm loginForm) {
        Optional<User> userCandidate = usersRepository.findByLogin(loginForm.getLogin());

        if (userCandidate.isPresent()) {
            User user = userCandidate.get();
            if (encoder.matches(loginForm.getPassword(), user.getPasswordHash())) {
                String value = UUID.randomUUID().toString();
                Token token = Token.builder()
                        .createdAt(LocalDateTime.now())
                        .expiredDateTime(LocalDateTime.now().plusSeconds(expiredSecondsForToken))
                        .value(value)
                        .user(user)
                        .build();
                tokensRepository.save(token);
                return TokenDto.from(token);
            }
        }
        throw new BadCredentialsException("Incorrect login or password");
    }
}
