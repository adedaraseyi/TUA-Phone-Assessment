package com.tua.apps.tuaphoneapi.security.pojo;

import com.tua.apps.core.phone.entity.CoreAccount;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class ImplicitAccountAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1L;
    private final CoreAccount coreAccount;

    public ImplicitAccountAuthenticationToken(CoreAccount coreAccount) {
        super(List.of());
        this.coreAccount = coreAccount;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public CoreAccount getPrincipal() {
        return coreAccount;
    }

    @Override
    public String getName() {
        return coreAccount.getId().toString();
    }
}
