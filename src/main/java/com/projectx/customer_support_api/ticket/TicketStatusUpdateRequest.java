package com.projectx.customer_support_api.ticket;

import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateRequest(
    @NotNull TicketStatus status) {
}
