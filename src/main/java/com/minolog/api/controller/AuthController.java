package com.minolog.api.controller;

import com.minolog.api.domain.Users;
import com.minolog.api.exception.InvalidSigninInformation;
import com.minolog.api.repository.UserRepository;
import com.minolog.api.request.Login;
import com.minolog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
//    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public Users login(@RequestBody Login login) {
        Users user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);
        log.info("[@Login] = {}", user);
        return user;


//        Long userId = authService.signin(login);
//
//        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());
//
//        String jws = Jwts.builder()
//                .setSubject(String.valueOf(userId))
//                .signWith(key)
//                .setIssuedAt(new Date())
//                .compact();
//
//        return new SessionResponse(jws);
    }

//    @PostMapping("/auth/signup")
//    public void signup(@RequestBody Signup signup) {
//        authService.signup(signup);
//    }

}
