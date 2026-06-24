package com.projectx.customer_support_api.ticket;

public record TicketFilterRequest(
        TicketStatus status,
        TicketPriority priority,
        Boolean isSlaViolated,
        String subject,
        int page,
        int size
) {
    public TicketFilterRequest {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
    }
}
