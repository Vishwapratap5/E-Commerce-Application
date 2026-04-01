package com.guru.ecommerce.Security;

import com.guru.ecommerce.DAO.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.guru.ecommerce.Model.User foundUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Customer "+username+" not found"));
        return CustomUserDetailsImpl.build(foundUser);

    }
}
