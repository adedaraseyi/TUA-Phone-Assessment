package com.tua.apps.tuaphoneapi.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneNumberPair {
    String fromPhoneNumber;

    String toPhoneNumber;

    @Builder.Default
    Instant expiryAt = Instant.now().plus(4, ChronoUnit.HOURS);

    public boolean stillValid(Instant now) {
        return expiryAt.isAfter(now);
    }

}
