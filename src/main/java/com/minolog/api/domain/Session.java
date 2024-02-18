package com.minolog.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accessToken;
//    private LocalDateTime
    @ManyToOne
    private Users user;

    @Builder
    public Session(Users user) {
        this.accessToken = UUID.randomUUID().toString();
        this.user = user;
    }
}
