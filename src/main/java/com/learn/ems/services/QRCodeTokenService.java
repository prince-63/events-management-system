package com.learn.ems.services;

import com.learn.ems.entity.Event;
import com.learn.ems.entity.QRCodeToken;
import com.learn.ems.entity.User;

/**
 * Service interface for managing qr-code-tokens of event check-in.
 */
public interface QRCodeTokenService {

    /**
     * generate a token
     * @param user - object of an user
     * @param event - object of an event
     * @return - QRCodeToken object
     */
    QRCodeToken generateToken(User user, Event event);

    /**
     * validate a token
     * @param token - token string
     * @return - true, when right token found, false when invalid
     */
    boolean validateToken(String token);

    /**
     * get QRCodeToken by token string
     * @param token - token string
     * @return - QRCodeToken object
     */
    QRCodeToken getToken(String token);

    /**
     * Invalidate Token by token string
     * @param token - token string
     */
    void invalidateToken(String token);

}
