package com.michaldrozd.frauddetection.domain;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents a merchant that processes transactions.
 */
@Getter
@Setter
@Node
public class Merchant {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String merchantId;
    private String address;
    private String phoneNumber;
    private String email;

    @Relationship(type = "PROCESSED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Transaction> transactions;
}