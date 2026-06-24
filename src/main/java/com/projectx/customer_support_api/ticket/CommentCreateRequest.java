package com.projectx.customer_support_api.ticket;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @NotBlank(message = "Текст комментария не может быть пустым")
        String text,

        @NotBlank(message = "Имя автора должно быть указано")
        String authorName
) {
}
