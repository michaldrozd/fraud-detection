package com.michaldrozd.frauddetection.config;

import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Neo4jSchemaConfig {

    private final Driver neo4jDriver;

    public Neo4jSchemaConfig(Driver neo4jDriver) {
        this.neo4jDriver = neo4jDriver;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createSchema() {
        try (Session session = neo4jDriver.session()) {
            // TODO: This will fail if it exists already
            // Create a unique constraint on the User node's id property
            String createUserConstraintQuery = "CREATE CONSTRAINT user_id_unique FOR (u:User) REQUIRE u.id IS UNIQUE";
            session.run(createUserConstraintQuery);
            log.info("Created unique constraint on User.id");

            // Create a unique constraint on the Transaction node's id property
            String createTransactionConstraintQuery = "CREATE CONSTRAINT transaction_id_unique FOR (t:Transaction) REQUIRE t.id IS UNIQUE";
            session.run(createTransactionConstraintQuery);
            log.info("Created unique constraint on Transaction.id");

            // Add more schema creation queries as needed

        } catch (Exception e) {
            log.error("Error creating schema", e);
        }
    }
}