package com.tua.apps.core.phone.config;

import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.core.phone.service.CorePhoneServiceImpl;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.tua.apps.core.phone.repository")
@EntityScan("com.tua.apps.core.phone.entity")
public class CorePhoneAutoConfiguration {

    @Bean
    public CorePhoneService corePhoneService() {
        return new CorePhoneServiceImpl();
    }
}
