package com.projectx.customer_support_api.ticket;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String subject,
        String description,
        TicketStatus status,
        Long customerId,
        TicketPriority priority,
        LocalDateTime deadlineAt,
        boolean isSlaViolated
) {
}
