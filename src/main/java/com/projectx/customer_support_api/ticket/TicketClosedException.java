package com.projectx.customer_support_api.ticket;

public class TicketClosedException extends RuntimeException {

    public TicketClosedException(Long ticketId) {
        super("Ticket with id " + ticketId + " is closed and cannot be commented");
    }
}
