package com.example.chatapp.util;

import com.example.chatapp.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public UserPrincipal principal(Authentication authentication) {
        return (UserPrincipal) authentication.getPrincipal();
    }
}
