package com.example.lms.domain.registration.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegistrationStatus {
    REGISTERED("REGISTERED", "수강 신청 완료"),
    APPROVED("APPROVED", "수강 신청 승인"),
    CANCELED("CANCELED", "수강 신청 취소");

    private final String code;
    private final String description;
}
