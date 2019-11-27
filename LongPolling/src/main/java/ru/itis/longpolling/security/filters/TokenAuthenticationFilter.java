package ru.itis.longpolling.security.filters;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.itis.longpolling.security.auth.TokenAuthentication;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationFilter implements Filter {
    private final static String AUTH_HEADER = "AUTH";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // вытаскиваем запрос
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // вытаскиваем заголовок с токеном
        String token = request.getHeader(AUTH_HEADER);
        // если заголовок содержит что-либо
        if (token != null) {
            // создаем объект токен-аутентификации
            TokenAuthentication authentication = new TokenAuthentication();
            // в него кладем токен
            authentication.setToken(token);
            // отдаем контексту
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // отдаем запрос дальше (его встретит либо другой фильтр, либо что-то еще)
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
