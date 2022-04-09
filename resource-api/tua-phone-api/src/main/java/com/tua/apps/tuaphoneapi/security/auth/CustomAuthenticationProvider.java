package com.tua.apps.tuaphoneapi.security.auth;

import com.tua.apps.tuaphoneapi.security.pojo.CustomUserDetails;
import com.tua.apps.tuaphoneapi.security.pojo.ImplicitAccountAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public CustomAuthenticationProvider(){};

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if(!StringUtils.hasText(password)) {
            throw new BadCredentialsException("Password is empty.");
        }

        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Password is incorrect.");
        }

        return addTokenToContext(new ImplicitAccountAuthenticationToken(userDetails.getAccount()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private static ImplicitAccountAuthenticationToken addTokenToContext(ImplicitAccountAuthenticationToken accountAuthenticationToken) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(accountAuthenticationToken);
        SecurityContextHolder.setContext(context);
        return accountAuthenticationToken;
    }

}
