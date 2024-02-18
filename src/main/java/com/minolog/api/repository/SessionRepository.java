package com.minolog.api.repository;

import com.minolog.api.domain.Post;
import com.minolog.api.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
