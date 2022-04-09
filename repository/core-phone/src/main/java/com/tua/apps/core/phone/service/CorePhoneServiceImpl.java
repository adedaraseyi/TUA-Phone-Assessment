package com.tua.apps.core.phone.service;

import com.querydsl.core.types.Predicate;
import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.entity.CorePhoneNumber;
import com.tua.apps.core.phone.repository.CoreAccountRepository;
import com.tua.apps.core.phone.repository.CorePhoneNumberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class CorePhoneServiceImpl implements CorePhoneService {
    @Autowired
    private CoreAccountRepository coreAccountRepository;
    @Autowired
    private CorePhoneNumberRepository corePhoneNumberRepository;

    @Override
    public Optional<CoreAccount> findAccountById(Integer id) {
        return coreAccountRepository.findById(id);
    }

    @Override
    public Optional<CoreAccount> findAccountByUsername(String username) {
        return coreAccountRepository.findByUsername(username);
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

    @Override
    public Optional<CorePhoneNumber> findPhoneNumberById(Integer id) {
        return corePhoneNumberRepository.findById(id);
    }

    @Override
    public CorePhoneNumber saveOrUpdate(CorePhoneNumber corePhoneNumber) {
        return corePhoneNumberRepository.save(corePhoneNumber);
    }

    @Override
    public Iterable<CorePhoneNumber> saveOrUpdatePhoneNumbers(Iterable<CorePhoneNumber> corePhoneNumbers) {
        return corePhoneNumberRepository.saveAll(corePhoneNumbers);
    }

    @Override
    public Optional<CorePhoneNumber> findOnePhoneNumber(Predicate predicate) {
        return corePhoneNumberRepository.findOne(predicate);
    }

    @Override
    public boolean phoneNumberExists(Predicate predicate) {
        return corePhoneNumberRepository.exists(predicate);
    }

    @Override
    public long phoneNumberCount(Predicate predicate) {
        return corePhoneNumberRepository.count(predicate);
    }

    @Override
    public Iterable<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate) {
        return corePhoneNumberRepository.findAll(predicate);
    }

    @Override
    public Page<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate, Pageable pageable) {
        return corePhoneNumberRepository.findAll(predicate, pageable);
    }

    @Override
    public Iterable<CorePhoneNumber> findAllPhoneNumbers(Predicate predicate, Sort sort) {
        return corePhoneNumberRepository.findAll(predicate, sort);
    }

}
