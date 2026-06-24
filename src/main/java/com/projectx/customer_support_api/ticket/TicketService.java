package com.projectx.customer_support_api.ticket;

import com.projectx.customer_support_api.common.InvalidStatusTransitionException;
import com.projectx.customer_support_api.config.RabbitMqConfig;
import com.projectx.customer_support_api.customer.Customer;
import com.projectx.customer_support_api.customer.CustomerNotFoundException;
import com.projectx.customer_support_api.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public TicketResponse create(Long customerId, TicketCreateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        TicketPriority priority = request.priority() != null ? request.priority() : TicketPriority.MEDIUM;
        LocalDateTime deadline = LocalDateTime.now().plus(priority.getResponseWindow());

        Ticket ticket = new Ticket(
                null,
                request.subject(),
                request.description(),
                TicketStatus.NEW,
                customer
        );

        ticket.setPriority(priority);
        ticket.setDeadlineAt(deadline);
        ticket.setSlaViolated(false);

        Ticket savedTicket = ticketRepository.save(ticket);

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE_NAME,
                RabbitMqConfig.ROUTING_KEY,
                savedTicket.getId()
        );

        return toResponse(savedTicket);
    }

    private TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCustomer().getId(),
                ticket.getPriority(),
                ticket.getDeadlineAt(),
                ticket.isSlaViolated()
        );
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findAllByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return ticketRepository.findAllByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse findById(Long customerId, Long ticketId) {
        Ticket ticket = ticketRepository.findByIdAndCustomerId(ticketId, customerId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        return toResponse(ticket);
    }

    @Transactional
    public TicketResponse updateStatus(
            Long customerId,
            Long ticketId,
            TicketStatusUpdateRequest request
    ) {
        Ticket ticket = ticketRepository.findByIdAndCustomerId(ticketId, customerId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus targetStatus = request.status();

        if (!currentStatus.canTransitionTo(targetStatus)) {
            throw new InvalidStatusTransitionException(currentStatus, targetStatus);
        }

        ticket.setStatus(targetStatus);
        Ticket savedTicket = ticketRepository.save(ticket);

        return toResponse(savedTicket);
    }

    @Transactional
    public CommentResponse addComment(Long customerId, Long ticketId, CommentCreateRequest request) {
        Ticket ticket = ticketRepository.findByIdAndCustomerId(ticketId, customerId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new TicketClosedException(ticketId);
        }

        Comment comment = new Comment(
                null,
                request.text(),
                LocalDateTime.now(),
                ticket,
                request.authorName()
        );

        Comment savedComment = commentRepository.save(comment);

        return toCommentResponse(savedComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findCommentsByTicketId(Long customerId, Long ticketId) {

        if (!ticketRepository.existsByIdAndCustomerId(ticketId, customerId)) {
            throw new TicketNotFoundException(ticketId);
        }

        return commentRepository.findAllByTicketIdOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(this::toCommentResponse)
                .toList();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt(),
                comment.getAuthorName()
        );
    }

    @Transactional(readOnly = true)
    public Page<TicketResponse> findAllFiltered(TicketFilterRequest filter) {
        Pageable pageable = PageRequest.of(
                filter.page(),
                filter.size(),
                Sort.by(Sort.Direction.ASC, "deadlineAt")
        );

        Specification<Ticket> spec = TicketSpecification.filterBy(filter);

        return ticketRepository.findAll(spec, pageable)
                .map(this::toResponse);
    }
}
