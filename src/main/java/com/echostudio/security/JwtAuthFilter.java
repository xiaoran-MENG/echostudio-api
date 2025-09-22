package com.echostudio.security;

import com.echostudio.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.echostudio.constant.SecurityConstants.*;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Arrays.stream(REQUEST_PATHS_PERMITTED).anyMatch(path -> request.getRequestURI().contains(path))) {
            filterChain.doFilter(request, response);
            return;
        }

        String email, token;
        var header = request.getHeader(AUTH_HEADER_KEY);
        if (header != null && header.startsWith(JWT_TOKEN_PREFIX)) {
            token = header.substring(JWT_TOKEN_PREFIX.length());
            try {
                email = jwtUtils.email(token);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Unable to get JWT token");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("JWT token does not start with Bearer token");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(email);
            if (jwtUtils.verifyToken(token, userDetails)) {
                var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                var webAuthDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                authToken.setDetails(webAuthDetails);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
