package com.guru.ecommerce.Utils;

import com.guru.ecommerce.DAO.UserRepository;
import com.guru.ecommerce.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UserRepository userRepository;

    public String loggedInEmail() {
        User user=loggedInUser();
        return user.getEmail();
    }

    public User loggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName()).orElseThrow(()->new UsernameNotFoundException("Username not found"));
    }

    public Long loggedInUserId(){
        User user=loggedInUser();
        return user.getUserId();
    }
}
