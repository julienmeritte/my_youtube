package com.etna.myapi.configurations;

import com.etna.myapi.Exception.CustomResourceException;
import com.etna.myapi.entity.Eusers;
import com.etna.myapi.repositories.Rusers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private Rusers userRepository;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws CustomResourceException {
        final Eusers user;
        try {
            user = userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new CustomResourceException();
        }


        if (user == null) {
            throw new CustomResourceException();
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getPassword())
                .authorities(user.getRoles())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
