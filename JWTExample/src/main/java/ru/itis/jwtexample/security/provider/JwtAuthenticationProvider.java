package ru.itis.jwtexample.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.itis.jwtexample.security.auth.JwtAuthentication;
import ru.itis.jwtexample.security.details.UserDetailsImpl;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService service;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // делаем явное преобразование для работы с TokenAuthentication
        JwtAuthentication jwtAuthentication
                = (JwtAuthentication)authentication;
        // загружаем данные безопасности пользователя из UserDetailsService
        // по токену достали пользователя из БД
        UserDetailsImpl userDetails = (UserDetailsImpl) service.loadUserByUsername(jwtAuthentication.getLogin());
        userDetails.setToken(jwtAuthentication.getToken());
        // если данные пришли
        if (userDetails != null) {
            jwtAuthentication.setUserDetails(userDetails);

            // говорим, что с ним все окей
            jwtAuthentication.setAuthenticated(true);
        } else {
            throw new BadCredentialsException("Incorrect Token");
        }
        // возвращаем объект SecurityContext-у
        return jwtAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthentication.class.equals(authentication);
    }
}