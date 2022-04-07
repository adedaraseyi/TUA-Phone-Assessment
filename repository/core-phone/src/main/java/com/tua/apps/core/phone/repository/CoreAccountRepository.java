package com.tua.apps.core.phone.repository;

import com.tua.apps.core.phone.enitity.CoreAccount;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoreAccountRepository extends PagingAndSortingRepository<CoreAccount, Long>, QuerydslPredicateExecutor<CoreAccount> {
}