package com.projectx.customer_support_api.ticket;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    List<Ticket> findAllByCustomerId(Long customerId);

    Optional<Ticket> findByIdAndCustomerId(Long id, Long customerId);

    boolean existsByIdAndCustomerId(Long ticketId, Long customerId);

    List<Ticket> findAllByStatusInAndDeadlineAtBeforeAndIsSlaViolatedFalse(
            Collection<TicketStatus> statuses,
            LocalDateTime dateTime
    );
}
