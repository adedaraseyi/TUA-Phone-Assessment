package com.tua.apps.core.phone.repository;

import com.tua.apps.core.phone.entity.CoreAccount;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CoreAccountRepository extends PagingAndSortingRepository<CoreAccount, Integer>, QuerydslPredicateExecutor<CoreAccount> {
    Optional<CoreAccount> findByUsername(String authId);
}