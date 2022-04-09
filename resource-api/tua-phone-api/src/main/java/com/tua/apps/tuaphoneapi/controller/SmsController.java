package com.tua.apps.tuaphoneapi.controller;

import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.library.exception.ApiException;
import com.tua.apps.pojo.GenericResponse;
import com.tua.apps.tuaphoneapi.dto.PhoneNumberPair;
import com.tua.apps.tuaphoneapi.dto.requests.InboundSMSRequest;
import com.tua.apps.tuaphoneapi.dto.requests.OutboundSMSRequest;
import com.tua.apps.tuaphoneapi.services.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class SmsController {
    @Autowired
    private SmsService service;

     @ResponseStatus(HttpStatus.OK)
     @PostMapping("/inbound/sms")
     public GenericResponse inboundSms(@AuthenticationPrincipal CoreAccount account, @Valid @RequestBody InboundSMSRequest request) {
         if (!service.existsByAccountIdAndPhoneNumber(account.getId(), request.getTo())) {
            throw new ApiException("to parameter not found");
        }

        List<String> stopStrings = List.of("STOP", "STOP\\n", "STOP\\r", "STOP\\r\\n");
        if (stopStrings.contains(request.getText())) {
            service.addPhoneNumberPair(request.getFrom(), request.getTo());
        }

        return GenericResponse.builder().message("inbound sms ok").build();
    }

     @ResponseStatus(HttpStatus.OK)
     @PostMapping("/outbound/sms")
     public GenericResponse outboundSms(@AuthenticationPrincipal CoreAccount account, @Valid @RequestBody OutboundSMSRequest request) {
         Optional<PhoneNumberPair> stoppedPhoneOptional = Optional.ofNullable(service.getPhoneNumberPair(request.getFrom(), request.getTo()));
         stoppedPhoneOptional.ifPresent(phoneNumberPair -> {
             if (!phoneNumberPair.stillValid(Instant.now())) {
                 service.removePhoneNumberPair(phoneNumberPair.getFromPhoneNumber(), phoneNumberPair.getToPhoneNumber());
                 return;
             }

             throw new ApiException(String.format("sms from %s to %s blocked by STOP request", request.getFrom(), request.getTo()));
         });

         if (!service.existsByAccountIdAndPhoneNumber(account.getId(), request.getFrom())) {
             throw new ApiException("from parameter not found");
         }

         return service.outboundSms(request);
    }
}
