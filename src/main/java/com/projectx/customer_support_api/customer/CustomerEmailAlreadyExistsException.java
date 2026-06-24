package com.projectx.customer_support_api.customer;

public class CustomerEmailAlreadyExistsException extends RuntimeException {

    public CustomerEmailAlreadyExistsException(String email) {
        super("Customer with email " + email + " already exists");
    }
}
