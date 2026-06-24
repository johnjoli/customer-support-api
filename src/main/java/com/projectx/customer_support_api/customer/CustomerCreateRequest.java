package com.projectx.customer_support_api.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank(message = "ФИО не должно быть пустым")
        @Size(min = 2, max = 150, message = "ФИО должно быть от 2 до 150 символов")
        String fullName,

        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный формат email")
        String email
    ) {

}

