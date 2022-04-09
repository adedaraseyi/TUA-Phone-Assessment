package com.tua.apps.tuaphoneapi.domain;

import com.querydsl.core.BooleanBuilder;
import com.tua.apps.core.phone.entity.CorePhoneNumber;
import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.tuaphoneapi.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.tua.apps.core.phone.entity.QCorePhoneNumber.corePhoneNumber;
@Component
public class PhoneITFactory {
    @Autowired
    private CorePhoneService corePhoneService;

    public CorePhoneNumber getAnyPhoneNumberByAccountId(Integer accountId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(corePhoneNumber.accountId.eq(accountId));

        return RandomUtil.random(StreamSupport
                .stream(corePhoneService.findAllPhoneNumbers(builder).spliterator(), false)
                .collect(Collectors.toList()));
    }

}
