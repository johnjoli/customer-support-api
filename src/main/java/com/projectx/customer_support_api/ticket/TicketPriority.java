package com.projectx.customer_support_api.ticket;

import java.time.Duration;

public enum TicketPriority {
    LOW(Duration.ofHours(24)),
    MEDIUM(Duration.ofHours(8)),
    HIGH(Duration.ofHours(2)),
    CRITICAL(Duration.ofMinutes(30));

    private final Duration responseWindow;

    TicketPriority(Duration responseWindow) {
        this.responseWindow = responseWindow;
    }

    public Duration getResponseWindow() {
        return responseWindow;
    }
}
