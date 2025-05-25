package com.studyplatform.repository;

import com.studyplatform.model.User;
import com.studyplatform.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    List<VerificationToken> findByUser(User user);
    Optional<VerificationToken> findByUserAndUsed(User user, boolean used);
}