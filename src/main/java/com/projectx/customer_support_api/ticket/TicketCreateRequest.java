package com.projectx.customer_support_api.ticket;

import jakarta.validation.constraints.NotBlank;

public record TicketCreateRequest(
    @NotBlank String subject,
    @NotBlank String description,
    TicketPriority priority
) {

    public TicketCreateRequest {
        if (priority == null) {
            priority = TicketPriority.MEDIUM;
        }
    }

}
