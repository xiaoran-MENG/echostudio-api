package com.echostudio.service;

import com.echostudio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

import static com.echostudio.constant.SecurityConstants.ROLE_PREFIX;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email " + email));
        return new User(user.getEmail(), user.getPassword(), this.authorities(user));
    }

    private Collection<? extends GrantedAuthority> authorities(com.echostudio.document.User user) {
        var authority = new SimpleGrantedAuthority(ROLE_PREFIX + user.getRole().name());
        return Collections.singletonList(authority);
    }
}
