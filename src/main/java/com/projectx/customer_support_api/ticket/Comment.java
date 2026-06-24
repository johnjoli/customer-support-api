package com.projectx.customer_support_api.ticket;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private String authorName;

    public Comment() {}

    public Comment(Long id, String text, LocalDateTime createdAt, Ticket ticket, String authorName) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.ticket = ticket;
        this.authorName = authorName;
    }
}
