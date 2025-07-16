package com.learn.ems.repositories;

import com.learn.ems.entity.QRCodeToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRCodeTokenRepository extends JpaRepository<QRCodeToken, Long> {
    Optional<QRCodeToken> findByToken(String token);
}
