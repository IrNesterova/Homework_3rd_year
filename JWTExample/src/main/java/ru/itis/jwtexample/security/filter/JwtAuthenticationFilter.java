package ru.itis.jwtexample.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.itis.jwtexample.security.auth.JwtAuthentication;
import ru.itis.jwtexample.security.provider.JwtAuthenticationProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {
    private final static String AUTH_HEADER = "AUTH";

    @Autowired
    private JwtAuthenticationProvider provider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String headerValue = request.getHeader(AUTH_HEADER);
        JwtAuthentication authentication = new JwtAuthentication();

        if (headerValue != null) {
            authentication.setToken(headerValue);
            SecurityContextHolder.getContext().setAuthentication(provider.authenticate(authentication));
        } else {
            authentication.setAuthenticated(false);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}