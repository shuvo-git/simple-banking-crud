package com.jobayed.banking.repository.specification;

import com.jobayed.banking.controllers.dto.request.SearchRequest;
import com.jobayed.banking.entity.Account;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {
    public static Specification<Account> from(SearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getQuery() != null && !request.getQuery().isEmpty()) {
                String likePattern = "%" + request.getQuery().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("firstName")), likePattern),
                        cb.like(cb.lower(root.get("lastName")), likePattern),
                        cb.like(cb.lower(root.get("email")), likePattern),
                        cb.like(root.get("accountNumber"), likePattern)));
            }

            if (request.getAccountType() != null) {
                predicates.add(cb.equal(root.get("accountType"), request.getAccountType()));
            }

            if (request.getMinBalance() != null) {
                predicates.add(cb.ge(root.get("balance"), request.getMinBalance()));
            }

            if (request.getMaxBalance() != null) {
                predicates.add(cb.le(root.get("balance"), request.getMaxBalance()));
            }

            if (request.getBranch() != null && !request.getBranch().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("branch")), "%" + request.getBranch().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
