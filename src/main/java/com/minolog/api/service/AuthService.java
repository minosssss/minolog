package com.minolog.api.service;

import com.minolog.api.domain.Session;
import com.minolog.api.domain.Users;
import com.minolog.api.exception.InvalidSigninInformation;
import com.minolog.api.repository.UserRepository;
import com.minolog.api.request.Login;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long singIn(Login login) {
        Users user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        user.addSession();
        return user.getId();

    }
}
