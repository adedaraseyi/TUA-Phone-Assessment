package com.tua.apps.tuaphoneapi.domain;

import com.querydsl.core.BooleanBuilder;
import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.tuaphoneapi.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class AccountITFactory {
    @Autowired
    private CorePhoneService corePhoneService;

    public CoreAccount getAnyExistingAccount() {
        BooleanBuilder builder = new BooleanBuilder();
        return RandomUtil.random(StreamSupport
                .stream(corePhoneService.findAllAccounts(builder).spliterator(), false)
                .collect(Collectors.toList()));
    }

}
