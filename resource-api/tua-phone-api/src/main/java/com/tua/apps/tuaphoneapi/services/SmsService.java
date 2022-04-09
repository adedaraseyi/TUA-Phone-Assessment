package com.tua.apps.tuaphoneapi.services;

import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.pojo.GenericResponse;
import com.tua.apps.tuaphoneapi.dto.PhoneNumberPair;
import com.tua.apps.tuaphoneapi.dto.requests.OutboundSMSRequest;
import com.tua.apps.tuaphoneapi.ratelimit.RateLimited;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.tua.apps.core.phone.entity.QCorePhoneNumber.corePhoneNumber;
@Service
@RequiredArgsConstructor
public class SmsService {
    final CorePhoneService corePhoneService;
    final EntityManager entityManager;

    public boolean existsByAccountId (Integer accountId) {
        return corePhoneService.phoneNumberExists(corePhoneNumber.accountId.eq(accountId));
    }

    public boolean existsByAccountIdAndPhoneNumber (Integer accountId, String phoneNumber) {
        return corePhoneService.phoneNumberExists(corePhoneNumber.accountId.eq(accountId)
                .and(corePhoneNumber.number.equalsIgnoreCase(phoneNumber))
        );
    }

    @RateLimited(key = "#request.from", requestsPerUnit = 50, unit = ChronoUnit.DAYS)
    public GenericResponse outboundSms(@Valid @RequestBody OutboundSMSRequest request) {
        return GenericResponse.builder().message("outbound sms ok").build();
    }

    @Cacheable(value = "stopped_phone_number")
    public List<PhoneNumberPair> getPhoneNumberPairs() {
        return new ArrayList<>();
    }

    @Cacheable(value = "stopped_phone_number", key = "'from-'+ #from +'.to-' + #to")
    public PhoneNumberPair getPhoneNumberPair(String from, String to) {
        return null;
    }

    @CachePut(value = "stopped_phone_number", key = "'from-'+ #from +'.to-' + #to")
    public void addPhoneNumberPair(String from, String to) {
        //No persistence
    }

    @CacheEvict(value = "stopped_phone_number", key = "'from-'+ #from +'.to-' + #to")
    public void removePhoneNumberPair(String from, String to) {
        //No persistence
    }


}
