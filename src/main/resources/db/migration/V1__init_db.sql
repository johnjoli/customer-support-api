CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE ticket (
    id BIGSERIAL PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    status VARCHAR(50) NOT NULL,
    customer_id BIGINT NOT NULL,
    priority VARCHAR(50) NOT NULL,
    deadline_at TIMESTAMP NOT NULL,
    is_sla_violated BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_ticket_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    ticket_id BIGINT NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_comment_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id)
);