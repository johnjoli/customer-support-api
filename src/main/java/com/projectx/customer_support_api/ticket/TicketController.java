package com.projectx.customer_support_api.ticket;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse create(
            @PathVariable Long customerId,
            @Valid @RequestBody TicketCreateRequest request
    ) {
        return ticketService.create(customerId, request);
    }

    @GetMapping
    public List<TicketResponse> findAllByCustomerId(@PathVariable Long customerId) {
        return ticketService.findAllByCustomerId(customerId);
    }

    @GetMapping("/{ticketId}")
    public TicketResponse findById(
            @PathVariable Long customerId,
            @PathVariable Long ticketId
    ) {
        return ticketService.findById(customerId, ticketId);
    }

    @PatchMapping("/{ticketId}/status")
    public TicketResponse updateStatus(
            @PathVariable Long customerId,
            @PathVariable Long ticketId,
            @Valid @RequestBody TicketStatusUpdateRequest request
    ) {
        return ticketService.updateStatus(customerId, ticketId, request);
    }

    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long customerId,
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.addComment(customerId, ticketId, request));
    }

    @GetMapping("/{ticketId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long customerId,
            @PathVariable Long ticketId
    ) {
        return ResponseEntity.ok(ticketService.findCommentsByTicketId(customerId, ticketId));
    }
}

