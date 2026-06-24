package com.projectx.customer_support_api.ticket;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String text,
        LocalDateTime createdAt,
        String authorName
) {
}
