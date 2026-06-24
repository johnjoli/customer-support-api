package com.projectx.customer_support_api.common;

import com.projectx.customer_support_api.ticket.TicketStatus;

public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(TicketStatus current, TicketStatus target) {
        super(String.format("Запрещен переход из статуса %s в статус %s", current, target));
    }
}
