package com.tua.apps.tuaphoneapi.security.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.sendError(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase());
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("TalentUp Africa");
        super.afterPropertiesSet();
    }

}
