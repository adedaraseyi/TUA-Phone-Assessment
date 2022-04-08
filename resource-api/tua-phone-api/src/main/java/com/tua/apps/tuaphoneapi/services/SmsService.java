package com.tua.apps.tuaphoneapi.services;

import com.tua.apps.core.phone.service.CorePhoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class SmsService {
    final CorePhoneService corePhoneService;
    final EntityManager entityManager;


}
