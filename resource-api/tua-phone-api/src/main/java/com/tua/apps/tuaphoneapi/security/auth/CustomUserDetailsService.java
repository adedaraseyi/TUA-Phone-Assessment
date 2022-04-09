package com.tua.apps.tuaphoneapi.security.auth;

import com.tua.apps.core.phone.entity.CoreAccount;
import com.tua.apps.core.phone.service.CorePhoneService;
import com.tua.apps.tuaphoneapi.security.pojo.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private CorePhoneService corePhoneService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CoreAccount account = corePhoneService.findAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(account.getUsername());
        customUserDetails.setPassword(account.getAuthId());
        customUserDetails.setId(account.getId());
        customUserDetails.setAccount(account);

        return customUserDetails;
    }
}
