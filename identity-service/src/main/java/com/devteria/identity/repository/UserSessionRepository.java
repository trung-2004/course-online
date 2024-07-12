package com.devteria.identity.repository;

import com.devteria.identity.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    //UserSession findByToken(String token);
    UserSession findByUserId(String userId);
}
