package com.example.hostelmanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Optional<Cookie> jwtCookie = getJwtCookie(request);

        if (jwtCookie.isPresent()) {
            String token = jwtCookie.get().getValue();
            if (token != null && !token.isEmpty()) {
                request = new WrappedRequest(request, token); // ✅ Wrap request to include the Authorization header
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<Cookie> getJwtCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "token".equals(cookie.getName()))
                .findFirst();
    }

    // ✅ Custom HttpServletRequestWrapper to modify headers
    private static class WrappedRequest extends HttpServletRequestWrapper {
        private final String token;

        public WrappedRequest(HttpServletRequest request, String token) {
            super(request);
            this.token = token;
        }

        @Override
        public String getHeader(String name) {
            if ("Authorization".equalsIgnoreCase(name)) {
                return "Bearer " + token; // ✅ Add Authorization header
            }
            return super.getHeader(name);
        }
    }
}


