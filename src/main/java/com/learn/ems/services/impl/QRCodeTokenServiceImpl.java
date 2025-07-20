package com.learn.ems.services.impl;

import com.learn.ems.entity.Event;
import com.learn.ems.entity.QRCodeToken;
import com.learn.ems.entity.User;
import com.learn.ems.exceptions.NotFoundException;
import com.learn.ems.repositories.QRCodeTokenRepository;
import com.learn.ems.services.QRCodeTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class QRCodeTokenServiceImpl implements QRCodeTokenService {

    private final QRCodeTokenRepository qrCodeTokenRepository;

    @Override
    public QRCodeToken generateToken(User user, Event event) {
        // Generate a secure random token
        String rawToken = UUID.randomUUID() + "-" + user.getId() + "-" + event.getId();

        QRCodeToken token = QRCodeToken.builder()
                .token(rawToken)
                .used(false)
                .expiresAt(event.getEndTime()) // valid till end of the event
                .build();
        return qrCodeTokenRepository.save(token);
    }

    @Override
    public boolean validateToken(String token) {
        return qrCodeTokenRepository.findByToken(token)
                .filter(t -> t.getExpiresAt().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    @Override
    public QRCodeToken getToken(String token) {
        return qrCodeTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Qr token not found"));
    }

    @Override
    public void invalidateToken(String token) {
        qrCodeTokenRepository.findByToken(token).ifPresent(qrCodeTokenRepository::delete);
    }

}
