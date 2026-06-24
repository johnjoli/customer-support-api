package com.projectx.customer_support_api.ticket;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("Должен выбросить TicketClosedException при попытке добавить комментарий в CLOSED тикет")
    void addComment_ShouldThrowException_WhenTicketIsClosed() {
        Long customerId = 1L;
        Long ticketId = 10L;
        CommentCreateRequest request = new CommentCreateRequest("Мой коммент", "Агент");

        Ticket closedTicket = new Ticket();
        closedTicket.setStatus(TicketStatus.CLOSED);

        when(ticketRepository.findByIdAndCustomerId(ticketId, customerId))
                .thenReturn(Optional.of(closedTicket));

        TicketClosedException exception = assertThrows(TicketClosedException.class, () -> {
            ticketService.addComment(customerId, ticketId, request);
        });

        assertEquals("Ticket with id 10 is closed and cannot be commented", exception.getMessage());

        verifyNoInteractions(commentRepository);
    }
}
