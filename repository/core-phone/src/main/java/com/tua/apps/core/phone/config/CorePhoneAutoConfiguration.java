package com.tua.apps.core.phone.config;

import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.core.phone.service.CorePhoneServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.tua.apps.core.phone.repository")
@EntityScan("com.tua.apps.core.phone.entity")
public class CorePhoneAutoConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource")
    @ConditionalOnMissingBean
    public DataSource coreDataSource() {
        return new DriverManagerDataSource();
    }

    @Bean
    @ConditionalOnMissingBean
    public JdbcTemplate coreDataSourceJdbcTemplate(@Qualifier("coreDataSource") DataSource coreDataSource) {
        return new JdbcTemplate(coreDataSource);
    }

    @Bean
    public CorePhoneService corePhoneService() {
        return new CorePhoneServiceImpl();
    }
}
