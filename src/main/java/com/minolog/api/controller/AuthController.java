package com.minolog.api.controller;

import com.minolog.api.domain.Users;
import com.minolog.api.exception.InvalidSigninInformation;
import com.minolog.api.repository.UserRepository;
import com.minolog.api.request.Login;
import com.minolog.api.response.SessionResponse;
import com.minolog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        Long id = authService.singIn(login);

        ResponseCookie cookie = ResponseCookie.from("SESSION", String.valueOf(id))
                .domain("localhost") // TODO 서버 환경에 따른 분리 필요
                .path("/")
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        log.info("[@Login Cookie] = {}", cookie.toString());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
    }

}
