package com.projectx.customer_support_api.customer;

public record CustomerResponse(
        Long id,
        String fullName,
        String email
) {
}
