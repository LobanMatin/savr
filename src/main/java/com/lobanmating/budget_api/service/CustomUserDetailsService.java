package com.lobanmating.budget_api.service;

import com.lobanmating.budget_api.model.User;
import com.lobanmating.budget_api.repository.UserRepository;
import com.lobanmating.budget_api.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with the following email not found: " + email));

        // TODO: add authorities for users in the future
        return new CustomUserDetails(user);
    }
}