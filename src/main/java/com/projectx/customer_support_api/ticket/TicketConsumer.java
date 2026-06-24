package com.projectx.customer_support_api.ticket;

import com.projectx.customer_support_api.config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketConsumer {

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void consumeTicketId(Long ticketId) {

        System.out.println("==============================================");
        System.out.println("КРОЛИК СРАБОТАЛ! Поступил новый тикет с ID: " + ticketId);
        System.out.println("Тут мы будем отправлять email кастомеру...");
        System.out.println("==============================================");
    }
}
