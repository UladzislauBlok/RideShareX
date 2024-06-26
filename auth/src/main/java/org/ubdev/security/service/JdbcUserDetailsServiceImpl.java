package org.ubdev.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.ubdev.user.repository.UserRepository;

@RequiredArgsConstructor
public class JdbcUserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.getUserByEmail(email);
        if (user.isEmpty())
            throw new UsernameNotFoundException(email);

        return user.get();
    }
}
