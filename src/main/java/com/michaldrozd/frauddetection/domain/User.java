package com.michaldrozd.frauddetection.domain;

import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Getter
@Setter
@Node
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.INCOMING)
    private Set<CreditCard> creditCards;

    @Relationship(type = "USED_BY", direction = Relationship.Direction.INCOMING)
    private Set<Device> devices;
}