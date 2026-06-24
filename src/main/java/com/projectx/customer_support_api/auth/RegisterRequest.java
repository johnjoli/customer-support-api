package com.projectx.customer_support_api.auth;

public record RegisterRequest(
        String username,
        String email,
        String password
) {
}
