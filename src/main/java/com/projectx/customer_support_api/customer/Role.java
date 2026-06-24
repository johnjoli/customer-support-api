package com.projectx.customer_support_api.customer;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CUSTOMER,
    ROLE_SUPPORT_AGENT;


    @Override
    public String getAuthority() {
        return name();
    }
}
