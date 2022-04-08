package com.tua.apps.core.phone.service;

import com.querydsl.core.types.Predicate;
import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.entity.CorePhoneNumber;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public interface CorePhoneService {
    Optional<CoreAccount> findAccountById(Long id);

    CoreAccount saveOrUpdate(CoreAccount coreAccount);

    Iterable<CoreAccount> saveOrUpdateAccounts(Iterable<CoreAccount> coreAccounts);

    Optional<CoreAccount> findOneAccount(Predicate predicate);

    boolean accountExists(Predicate predicate);

    long accountCount(Predicate predicate);

    Iterable<CoreAccount> findAllAccounts(Predicate predicate);

    Page<CoreAccount> findAllAccounts(Predicate predicate, Pageable pageable);

    Iterable<CoreAccount> findAllAccounts(Predicate predicate, Sort sort);

    Optional<CorePhoneNumber> findPhoneNumberById(Long id);

    CorePhoneNumber saveOrUpdate(CorePhoneNumber corePhoneNumber);

    Iterable<CorePhoneNumber> saveOrUpdatePhoneNumbers(Iterable<CorePhoneNumber> corePhoneNumbers);

    Optional<CorePhoneNumber> findOnePhoneNumber(Predicate predicate);

    boolean phoneNumberExists(Predicate predicate);

    long phoneNumberCount(Predicate predicate);

    Iterable<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate);

    Page<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate, Pageable pageable);

    Iterable<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate, Sort sort);
}
