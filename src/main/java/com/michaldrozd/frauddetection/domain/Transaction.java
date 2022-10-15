package com.michaldrozd.frauddetection.domain;

import com.michaldrozd.frauddetection.domain.types.TransactionStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents a transaction performed by a user.
 */
@Getter
@Setter
@Node
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;
    private String transactionId;
    private Double amount;
    private String currency;
    private LocalDateTime transactionDate;
    private TransactionStatus transactionStatus;

    @Relationship(type = "PERFORMED_BY", direction = Relationship.Direction.OUTGOING)
    private User user;

    @Relationship(type = "PROCESSED_BY", direction = Relationship.Direction.OUTGOING)
    private Merchant merchant;

    @Relationship(type = "USED", direction = Relationship.Direction.OUTGOING)
    private CreditCard creditCard;

    @Relationship(type = "CONDUCTED_WITH", direction = Relationship.Direction.OUTGOING)
    private Device device;
}