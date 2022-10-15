package com.michaldrozd.frauddetection.domain;

import java.util.HashSet;
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
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double price;
    private String category;

    @Relationship(type = "LIKES", direction = Relationship.Direction.INCOMING)
    private Set<User> likedByUsers;

    @Relationship(type = "VIEWED", direction = Relationship.Direction.INCOMING)
    private Set<User> viewedByUsers;

    public Product() {
        this.likedByUsers = new HashSet<>();
        this.viewedByUsers = new HashSet<>();
    }
}