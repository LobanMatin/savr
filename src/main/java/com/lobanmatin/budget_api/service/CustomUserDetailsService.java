package com.lobanmatin.budget_api.service;

import com.lobanmatin.budget_api.model.User;
import com.lobanmatin.budget_api.repository.UserRepository;
import com.lobanmatin.budget_api.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with the following email not found: " + email));

        return new CustomUserDetails(user);
    }
}