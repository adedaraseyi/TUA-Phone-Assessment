package com.tua.apps.tuaphoneapi.ratelimit;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

/**
 * Limits are that server accepts requests from given client (determined by clientInfoSource)
 * If rate is exceeded ApiException is thrown
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimited {

    /**
     * @return rate limiter identifier
     */
    String key() default "";

    /**
     * @return rate limit in queries per time unit
     */
    int requestsPerUnit();

    /**
     * @return time unit for rate limit
     */
    long timeUnit();

    /**
     * @return time unit
     */
    ChronoUnit unit();

    /**
     * @return client info source
     */
    ClientInfoSource clientInfoSource() default ClientInfoSource.PHONE_NUMBER;

    enum ClientInfoSource {
        PHONE_NUMBER
    }

}
