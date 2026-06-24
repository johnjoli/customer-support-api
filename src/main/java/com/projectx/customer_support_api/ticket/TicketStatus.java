package com.projectx.customer_support_api.ticket;

public enum TicketStatus {
    NEW,
    IN_PROGRESS,
    RESOLVED,
    CLOSED;

    public boolean canTransitionTo(TicketStatus target) {
        if (this == target) {
            return true;
        }

        return switch (this) {
            case NEW -> target == IN_PROGRESS || target == CLOSED;
            case IN_PROGRESS -> target == RESOLVED || target == CLOSED;
            case RESOLVED -> target == IN_PROGRESS || target == CLOSED;
            case CLOSED -> false;
        };
    }
}
