package com.example.chatapp.service;

import com.example.chatapp.dto.AuthResponse;
import com.example.chatapp.dto.LoginRequest;
import com.example.chatapp.dto.RegisterRequest;
import com.example.chatapp.entity.Role;
import com.example.chatapp.entity.User;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.mapper.AppMapper;
import com.example.chatapp.repository.UserRepository;
import com.example.chatapp.security.JwtService;
import com.example.chatapp.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AppMapper mapper;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       JwtService jwtService, AppMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mapper = mapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already taken");
        }
        var user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user = userRepository.save(user);
        var principal = new UserPrincipal(user);
        return new AuthResponse(jwtService.generateToken(principal), mapper.toUserResponse(user));
    }

    public AuthResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
        );
        var principal = (UserPrincipal) auth.getPrincipal();
        return new AuthResponse(jwtService.generateToken(principal), mapper.toUserResponse(principal.getUser()));
    }
}
