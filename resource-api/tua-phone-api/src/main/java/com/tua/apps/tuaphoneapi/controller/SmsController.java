package com.tua.apps.tuaphoneapi.controller;

import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.pojo.GenericResponse;
import com.tua.apps.tuaphoneapi.dto.requests.InboundSMSRequest;
import com.tua.apps.tuaphoneapi.dto.requests.OutboundSMSRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SmsController {
     final CorePhoneService corePhoneService;

     @ResponseStatus(HttpStatus.OK)
     @PostMapping("/inbound/sms")
     public GenericResponse inboundSms(@Valid @RequestBody InboundSMSRequest request) {

    }

     @ResponseStatus(HttpStatus.OK)
     @PostMapping("/outbound/sms")
     public GenericResponse outboundSms(@Valid @RequestBody OutboundSMSRequest request) {

    }
}
