package com.projectx.customer_support_api.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Component
public class TicketSlaScheduler {

    private static final Logger log = LoggerFactory.getLogger(TicketSlaScheduler.class);
    private final TicketRepository ticketRepository;

    public TicketSlaScheduler(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void checkSlaViolations() {
        log.info("Запуск фоновой проверки нарушений SLA для тикетов...");

        List<TicketStatus> activeStatuses = List.of(TicketStatus.NEW, TicketStatus.IN_PROGRESS);
        LocalDateTime now = LocalDateTime.now();

        List<Ticket> overdueTickets = ticketRepository
                .findAllByStatusInAndDeadlineAtBeforeAndIsSlaViolatedFalse(activeStatuses, now);

        if (overdueTickets.isEmpty()) {
            log.info("Просроченных тикетов не обнаружено.");
            return;
        }

        log.warn("Обнаружено просроченных тикетов: {}", overdueTickets.size());

        for (Ticket ticket : overdueTickets) {
            ticket.setSlaViolated(true);

            if (ticket.getPriority() == TicketPriority.LOW) {
                ticket.setPriority(TicketPriority.MEDIUM);
            } else if (ticket.getPriority() == TicketPriority.MEDIUM) {
                ticket.setPriority(TicketPriority.HIGH);
            }

            log.info("Тикет ID {} отмечен как нарушивший SLA. Текущий приоритет: {}",
                    ticket.getId(), ticket.getPriority());
        }
    }
}
