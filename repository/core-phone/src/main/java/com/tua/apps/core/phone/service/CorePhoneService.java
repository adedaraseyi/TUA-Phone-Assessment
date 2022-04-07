package com.tua.apps.core.phone.service;

import com.querydsl.core.types.Predicate;
import com.tua.apps.core.phone.enitity.CoreAccount;
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
}
