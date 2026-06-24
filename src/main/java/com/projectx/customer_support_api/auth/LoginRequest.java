package com.projectx.customer_support_api.auth;

public record LoginRequest(
        String username,
        String password
) {
}
