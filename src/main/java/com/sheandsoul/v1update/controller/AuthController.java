package com.sheandsoul.v1update.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sheandsoul.v1update.dto.AuthDto;
import com.sheandsoul.v1update.dto.LoginRequest;
import com.sheandsoul.v1update.dto.SignUpRequest;
import com.sheandsoul.v1update.entities.User;
import com.sheandsoul.v1update.repository.UserRepository;
import com.sheandsoul.v1update.services.MyUserDetailService;
import com.sheandsoul.v1update.util.JwtUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private MyUserDetailService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthDto(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.email()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        try {
            User user = userDetailsService.registerNewUser(signUpRequest);
            final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            final String jwt = jwtUtil.generateToken(userDetails);
            // In a real app, you'd return a JWT here instead of the full user object.
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully!",
                "userId", user.getId(),
                "jwt" , jwt           
                 ));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    
}
