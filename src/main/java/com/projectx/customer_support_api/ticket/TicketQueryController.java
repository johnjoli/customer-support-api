package com.projectx.customer_support_api.ticket;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketQueryController {

    private final TicketService ticketService;

    public TicketQueryController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<Page<TicketResponse>> findAllFiltered(
            @ModelAttribute TicketFilterRequest filter
    ) {
        return ResponseEntity.ok(ticketService.findAllFiltered(filter));
    }
}
