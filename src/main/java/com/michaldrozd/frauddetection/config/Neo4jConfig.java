package com.michaldrozd.frauddetection.config;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.AbstractNeo4jConfig;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.michaldrozd.frauddetection.repositories")
@EnableTransactionManagement
public class Neo4jConfig {
}