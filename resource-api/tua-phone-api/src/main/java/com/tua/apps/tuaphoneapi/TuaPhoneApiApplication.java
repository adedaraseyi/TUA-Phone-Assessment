package com.tua.apps.tuaphoneapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class TuaPhoneApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuaPhoneApiApplication.class, args);
    }

}
