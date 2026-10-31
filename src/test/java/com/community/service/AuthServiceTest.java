package com.community.service;

import com.community.dto.AuthResponse;
import com.community.dto.LoginRequest;
import com.community.dto.RegisterRequest;
import com.community.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Test
    void register_shouldReturnToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("alice");
        request.setPassword("123456");
        request.setNickname("爱丽丝");

        AuthResponse resp = authService.register(request);

        assertNotNull(resp.getToken());
        assertEquals("alice", resp.getUsername());
        assertEquals("爱丽丝", resp.getNickname());
    }

    @Test
    void login_shouldSucceed() {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("bob");
        register.setPassword("123456");
        authService.register(register);

        LoginRequest login = new LoginRequest();
        login.setUsername("bob");
        login.setPassword("123456");

        AuthResponse resp = authService.login(login);
        assertNotNull(resp.getToken());
    }

    @Test
    void register_duplicateUsername_shouldThrow() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("carol");
        request.setPassword("123456");
        authService.register(request);

        RegisterRequest duplicate = new RegisterRequest();
        duplicate.setUsername("carol");
        duplicate.setPassword("123456");

        assertThrows(BusinessException.class, () -> authService.register(duplicate));
    }

    @Test
    void login_wrongPassword_shouldThrow() {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("dave");
        register.setPassword("123456");
        authService.register(register);

        LoginRequest login = new LoginRequest();
        login.setUsername("dave");
        login.setPassword("wrong-password");

        assertThrows(BusinessException.class, () -> authService.login(login));
    }
}
