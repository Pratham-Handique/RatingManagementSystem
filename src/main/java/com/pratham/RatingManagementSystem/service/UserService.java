package com.pratham.RatingManagementSystem.service;

import com.pratham.RatingManagementSystem.dto.AuthRequest;
import com.pratham.RatingManagementSystem.dto.RegisterRequest;
import com.pratham.RatingManagementSystem.model.Role;
import com.pratham.RatingManagementSystem.model.User;
import com.pratham.RatingManagementSystem.repository.UserRepository;
import com.pratham.RatingManagementSystem.security.JwtUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       JwtUtil jwtUtil,
                       @Lazy AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));

        // 🆕 Set role from request, fallback to USER
        Role role = Role.USER;
        try {
            if (request.role != null) {
                role = Role.valueOf(request.role.toUpperCase());
            }
        } catch (IllegalArgumentException ignored) {
            // Invalid role provided, fallback to USER
        }

        user.setRole(role);
        userRepository.save(user);

        //return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    public String login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
        );
        User user = userRepository.findByEmail(request.email).orElseThrow();
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}
