package com.michaldrozd.frauddetection.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents a device used by a user.
 */
@Getter
@Setter
@Node
public class Device {
    @Id
    @GeneratedValue
    private Long id;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private Double latitude;
    private Double longitude;

    @Relationship(type = "CONDUCTED_WITH", direction = Relationship.Direction.INCOMING)
    private Transaction transaction;

    @Relationship(type = "USED_BY", direction = Relationship.Direction.OUTGOING)
    private User user;
}