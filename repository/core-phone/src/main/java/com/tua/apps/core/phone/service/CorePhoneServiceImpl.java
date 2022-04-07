package com.tua.apps.core.phone.service;

import com.querydsl.core.types.Predicate;
import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.repository.CoreAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class CorePhoneServiceImpl implements CorePhoneService {
    @Autowired
    private CoreAccountRepository coreAccountRepository;

    @Override
    public Optional<CoreAccount> findAccountById(Long id) {
        return coreAccountRepository.findById(id);
    }

    @Override
    public CoreAccount saveOrUpdate(CoreAccount coreAccount) {
        return coreAccountRepository.save(coreAccount);
    }

    @Override
    public Iterable<CoreAccount> saveOrUpdateAccounts(Iterable<CoreAccount> coreAccounts) {
        return coreAccountRepository.saveAll(coreAccounts);
    }

    @Override
    public Optional<CoreAccount> findOneAccount(Predicate predicate) {
        return coreAccountRepository.findOne(predicate);
    }

    @Override
    public boolean accountExists(Predicate predicate) {
        return coreAccountRepository.exists(predicate);
    }

    @Override
    public long accountCount(Predicate predicate) {
        return coreAccountRepository.count(predicate);
    }

    @Override
    public Iterable<CoreAccount> findAllAccounts(Predicate predicate) {
        return coreAccountRepository.findAll(predicate);
    }

    @Override
    public Page<CoreAccount> findAllAccounts(Predicate predicate, Pageable pageable) {
        return coreAccountRepository.findAll(predicate, pageable);
    }

    @Override
    public Iterable<CoreAccount> findAllAccounts(Predicate predicate, Sort sort) {
        return coreAccountRepository.findAll(predicate, sort);
    }

}
