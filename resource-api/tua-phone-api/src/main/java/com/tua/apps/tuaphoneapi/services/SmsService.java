package com.tua.apps.tuaphoneapi.services;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.pojo.GenericResponse;
import com.tua.apps.tuaphoneapi.dto.PhoneNumberPair;
import com.tua.apps.tuaphoneapi.dto.requests.OutboundSMSRequest;
import com.tua.apps.tuaphoneapi.ratelimit.RateLimited;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.tua.apps.core.phone.entity.QCorePhoneNumber.corePhoneNumber;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final CorePhoneService corePhoneService;

    private final Cache<String, PhoneNumberPair> phoneNumberPairCache = CacheBuilder.newBuilder()
            .expireAfterAccess(7, TimeUnit.DAYS)
            .build();

    public boolean existsByAccountIdAndPhoneNumber (Integer accountId, String phoneNumber) {
        return corePhoneService.phoneNumberExists(corePhoneNumber.accountId.eq(accountId)
                .and(corePhoneNumber.number.equalsIgnoreCase(phoneNumber))
        );
    }

    @RateLimited(key = "#request.from", requestsPerUnit = 50, timeUnit = 1, unit = ChronoUnit.DAYS)
    public GenericResponse outboundSms(@NonNull OutboundSMSRequest request) {
        return GenericResponse.builder().message("outbound sms ok").build();
    }

    public List<PhoneNumberPair> getPhoneNumberPairs() {
        return new ArrayList<>(phoneNumberPairCache.asMap().values());
    }

    public PhoneNumberPair getPhoneNumberPair(String from, String to) {
        return phoneNumberPairCache.getIfPresent(generateKey(from, to));
    }

    public void addPhoneNumberPair(String from, String to) {
        phoneNumberPairCache.put(generateKey(from, to), PhoneNumberPair.builder().fromPhoneNumber(from).toPhoneNumber(to).build());
    }

    public void removePhoneNumberPair(String from, String to) {
        phoneNumberPairCache.invalidate(generateKey(from, to));
    }

    private static String generateKey(String from, String to) {
        return "from-" + from + ".to-" + to;
    }

}
