package com.echostudio.controller;

import com.echostudio.dto.LoginRequest;
import com.echostudio.dto.LoginResponse;
import com.echostudio.dto.RegistrationRequest;
import com.echostudio.service.UserService;
import com.echostudio.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.echostudio.constant.SecurityConstants.DEFAULT_ROLE;
import static com.echostudio.constant.SecurityConstants.ROLE_PREFIX;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var email = request.getEmail();
        try {
            var authToken = new UsernamePasswordAuthenticationToken(email, request.getPassword());
            var auth = this.authManager.authenticate(authToken);
            var userDetails = (UserDetails) auth.getPrincipal();
            var role = auth.getAuthorities()
                    .stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(r -> r.startsWith(ROLE_PREFIX) ? r.substring(5) : r)
                    .orElse(DEFAULT_ROLE);
            var jwtToken = jwtUtils.token(userDetails, role);
            var response = new LoginResponse(jwtToken, email, role);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Email or password is incorrect");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            var response = this.userService.register(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
