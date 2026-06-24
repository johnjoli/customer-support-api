package com.projectx.customer_support_api.ticket;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class TicketSpecification {

    public static Specification<Ticket> filterBy(TicketFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.status() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"),filter.status()));
            }
            if (filter.priority() != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), filter.priority()));
            }
            if (filter.isSlaViolated() != null) {
                predicates.add(criteriaBuilder.equal(root.get("isSlaViolated"), filter.isSlaViolated()));
            }

            if (filter.subject() != null && !filter.subject().isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("subject")),
                        "%" + filter.subject().toLowerCase() + "%"
                ));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
