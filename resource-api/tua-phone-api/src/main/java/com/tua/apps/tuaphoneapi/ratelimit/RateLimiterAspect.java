package com.tua.apps.tuaphoneapi.ratelimit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tua.apps.library.exception.ApiException;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final Cache<String, CacheInfo> lastAccessCache = CacheBuilder.newBuilder()
            .expireAfterAccess(7, TimeUnit.DAYS)
            .build();

    private final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

    private final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Before("@annotation(rateLimited)")
    public void rateLimited(JoinPoint joinPoint, RateLimited rateLimited) {
        String key = null;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(signature.getMethod());
        if (parameterNames != null && parameterNames.length > 0) {
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = joinPoint.getArgs();

            for (int i = 0; i < args.length; i++) {
                context.setVariable(parameterNames[i],args[i]);
            }

            Object keyObj = spelExpressionParser.parseExpression(rateLimited.key()).getValue(context);
            if (keyObj != null) {
                key = keyObj.toString();
            }
        }

        if (!StringUtils.hasText(key)) {
            throw new ApiException("Rate limit key is not defined");
        }

        log.debug("Rate limiting for key {}", key);

        CacheInfo lastAccess = getLastAccess(key);
        if (lastAccess != null && shouldBlockAccess(lastAccess, rateLimited)) {
            throw new ApiException(String.format("limit reached for from %s", key));
        }

        if (ObjectUtils.isEmpty(lastAccess)) {
            lastAccess = CacheInfo.builder()
                    .requests(1)
                    .firstAccess(Instant.now())
                    .build();
        } else {
            lastAccess.setRequests(lastAccess.getRequests() + 1);
        }

        lastAccessCache.put(key, lastAccess);
    }

    private boolean shouldBlockAccess(CacheInfo cacheInfo, RateLimited rateLimited) {
        Duration interval = Duration.ofMillis(rateLimited.timeUnit() * rateLimited.unit().getDuration().toMillis());
        return cacheInfo.getRequests() >= rateLimited.requestsPerUnit() && cacheInfo.getFirstAccess().isAfter(Instant.now().minus(interval));
    }

    private CacheInfo getLastAccess(String key) {
        return lastAccessCache.getIfPresent(key);
    }

    @Data
    @SuperBuilder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    static class CacheInfo {
        Integer requests;
        Instant firstAccess;
    }
}
