package com.tua.apps.tuaphoneapi.ratelimit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tua.apps.library.exception.ApiException;
import lombok.RequiredArgsConstructor;
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
import org.springframework.util.StringUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimiterAspect {

    private final Cache<String, Instant> lastAccessCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.DAYS)
            .maximumSize(50_000)
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

        Duration interval = calculateInterval(rateLimited);
        Instant lastAccess = getLastAccess(key);
        if (lastAccess != null && shouldBlockAccess(lastAccess, interval)) {
            throw new ApiException(String.format("limit reached for from %s", key));
        }

        lastAccessCache.put(key, Instant.now());
    }

    private boolean shouldBlockAccess(Instant lastAccess, Duration intervalDuration) {
        return Instant.now().isBefore(lastAccess.plus(intervalDuration));
    }

    private Duration calculateInterval(RateLimited rateLimited) {
        double requestsPerUnit = rateLimited.requestsPerUnit();
        long millisInUnit = rateLimited.unit().getDuration().toMillis();
        double retryAfterMillis = millisInUnit / requestsPerUnit;
        return Duration.of((long) retryAfterMillis, ChronoUnit.MILLIS);
    }

    private Instant getLastAccess(String key) {
        return lastAccessCache.getIfPresent(key);
    }

}
