package com.learn.ems.mapper;

import com.learn.ems.dto.RegistrationResponseDTO;
import com.learn.ems.entity.Registration;

public class RegistrationMapper {

    public static RegistrationResponseDTO toDTO(Registration registration) {
        return RegistrationResponseDTO.builder()
                .id(registration.getId())
                .eventId(registration.getEvent().getId())
                .userId(registration.getUser().getId())
                .checkedIn(registration.isCheckedIn())
                .registeredAt(registration.getRegisteredAt())
                .qrCodeTokenId(registration.getQrCodeToken().getId())
                .build();
    }

}
