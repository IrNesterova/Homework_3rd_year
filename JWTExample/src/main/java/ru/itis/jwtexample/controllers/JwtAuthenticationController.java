package ru.itis.jwtexample.controllers;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itis.jwtexample.dto.TokenDto;
import ru.itis.jwtexample.forms.LoginForm;
import ru.itis.jwtexample.forms.SignUpForm;
import ru.itis.jwtexample.models.JwtResponse;
import ru.itis.jwtexample.models.User;
import ru.itis.jwtexample.security.details.JwtUserDetailsService;
import ru.itis.jwtexample.services.LoginService;
import ru.itis.jwtexample.services.UserService;
import ru.itis.jwtexample.util.JwtTokenUtil;

import javax.swing.text.html.Option;
import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    @PreAuthorize("permitAll()")
    @CrossOrigin
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody LoginForm loginForm) throws Exception {
        TokenDto token = loginService.login(loginForm);
        return ResponseEntity.ok(token);
    }

    @PostMapping(value = "/signUp")
    @PreAuthorize("permitAll()")
    @CrossOrigin
    public ResponseEntity<?> saveUser(@RequestBody SignUpForm signUpForm) throws Exception {
        userService.signUp(signUpForm);
        return ResponseEntity.ok().build();
    }
}