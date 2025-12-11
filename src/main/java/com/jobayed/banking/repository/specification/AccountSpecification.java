package com.jobayed.banking.repository.specification;

import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.entity.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {
    public static Specification<Account> from(SearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getQuery() != null && !request.getQuery().isEmpty()) {
                String likePattern = "%" + request.getQuery().toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                        criteriaBuilder.like(root.get("accountNumber"), likePattern)));
            }

            if (request.getAccountType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("accountType"), request.getAccountType()));
            }

            if (request.getMinBalance() != null) {
                predicates.add(criteriaBuilder.ge(root.get("balance"), request.getMinBalance()));
            }

            if (request.getMaxBalance() != null) {
                predicates.add(criteriaBuilder.le(root.get("balance"), request.getMaxBalance()));
            }

            if (request.getBranch() != null && !request.getBranch().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder
                        .lower(root.get("branch")), "%" + request.getBranch().toLowerCase() + "%"));
            }

            if (request.getStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), request.getStart().atStartOfDay()));
            }

            if (request.getEnd() != null) {
                // Compare < (End + 1 Day) at 00:00:00 to include the full end date
                predicates.add(criteriaBuilder.lessThan(root.get("createdAt"), request.getEnd()
                        .plusDays(1).atStartOfDay()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
