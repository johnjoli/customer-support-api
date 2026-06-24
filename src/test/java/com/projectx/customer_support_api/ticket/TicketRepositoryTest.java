package com.projectx.customer_support_api.ticket;

import com.projectx.customer_support_api.customer.Customer;
import com.projectx.customer_support_api.customer.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Должен находить только просроченные активные тикеты с флажком false")
    void shouldFindOverdueTickets() {
        LocalDateTime now = LocalDateTime.now();

        Customer customer = new Customer(null, "Имя", "email@example.com", "password123", Role.ROLE_CUSTOMER);
        entityManager.persistAndFlush(customer);

        Ticket overdueTicket = new Ticket(null, "Проблема", "Описание", TicketStatus.NEW, customer);
        overdueTicket.setPriority(TicketPriority.MEDIUM);
        overdueTicket.setDeadlineAt(now.minusHours(1));
        overdueTicket.setSlaViolated(false);
        entityManager.persistAndFlush(overdueTicket);

        Ticket closedTicket = new Ticket(null, "Закрыт", "Описание", TicketStatus.CLOSED, customer);
        closedTicket.setPriority(TicketPriority.MEDIUM);
        closedTicket.setDeadlineAt(now.minusHours(1));
        closedTicket.setSlaViolated(false);
        entityManager.persistAndFlush(closedTicket);

        List<Ticket> result = ticketRepository.findAllByStatusInAndDeadlineAtBeforeAndIsSlaViolatedFalse(
                List.of(TicketStatus.NEW, TicketStatus.IN_PROGRESS),
                now
        );

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSubject()).isEqualTo("Проблема");
    }
}