package com.sheandsoul.v1update.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sheandsoul.v1update.dto.SignUpRequest;
import com.sheandsoul.v1update.entities.User;
import com.sheandsoul.v1update.repository.UserRepository;

@Service
public class MyUserDetailService implements UserDetailsService {

@Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionService subscriptionService;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), new ArrayList<>());
    }

public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

public User registerNewUser(SignUpRequest signUpRequest) {
        User newUser = new User();
        newUser.setEmail(signUpRequest.email());
        newUser.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.password()));
        User savedUser = userRepository.save(newUser);
        subscriptionService.createSubscription(savedUser);
        return savedUser;
    }

}
