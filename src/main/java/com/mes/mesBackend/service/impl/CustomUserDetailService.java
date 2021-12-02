package com.mes.mesBackend.service.impl;

import com.amazonaws.services.kms.model.NotFoundException;
import com.mes.mesBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUserCode(username)
                .orElseThrow(() -> new NotFoundException("user does not exist. input userName: " + username));
    }
}
