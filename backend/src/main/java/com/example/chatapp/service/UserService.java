package com.example.chatapp.service;

import com.example.chatapp.dto.ProfileUpdateRequest;
import com.example.chatapp.dto.UserResponse;
import com.example.chatapp.exception.ApiException;
import com.example.chatapp.mapper.AppMapper;
import com.example.chatapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AppMapper mapper;

    public UserService(UserRepository userRepository, AppMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    public List<UserResponse> allUsers(Long currentUserId) {
        return userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .map(mapper::toUserResponse)
                .toList();
    }

    public List<UserResponse> search(String query, Long currentUserId) {
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query).stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .map(mapper::toUserResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toUserResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Transactional
    public UserResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
        user.setProfileImage(request.profileImage());
        return mapper.toUserResponse(user);
    }
}
