package com.tua.apps.core.phone.repository;

import com.tua.apps.core.phone.entity.CorePhoneNumber;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CorePhoneNumberRepository extends PagingAndSortingRepository<CorePhoneNumber, Integer>, QuerydslPredicateExecutor<CorePhoneNumber> {
}