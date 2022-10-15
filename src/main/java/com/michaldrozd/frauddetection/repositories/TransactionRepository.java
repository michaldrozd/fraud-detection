package com.michaldrozd.frauddetection.repositories;

import com.michaldrozd.frauddetection.domain.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends Neo4jRepository<Transaction, Long> {

    /**
     * Finds and returns a list of Transaction objects performed by a specific User within a given time window.
     * <p>
     * The method takes two parameters: the User's ID and a minimum LocalDateTime threshold. It queries the graph
     * database to retrieve all Transaction nodes connected to the User node via a PERFORMED_BY relationship, and
     * connected to a Device node via a CONDUCTED_WITH relationship. The query filters the transactions based on the
     * minimum transaction date provided.
     *
     * @param userId            The ID of the User for which to retrieve the transactions.
     * @param dateTimeThreshold The minimum LocalDateTime threshold for filtering transactions.
     *
     * @return A list of Transaction objects performed by the User within the specified time window, including the associated Device information.
     */
    @Query("MATCH (t:Transaction)-[:PERFORMED_BY]->(u:User), (t)-[:CONDUCTED_WITH]->(d:Device) WHERE u.id = $userId AND t.transactionDate >= $dateTimeThreshold RETURN t, d")
    List<Transaction> findTransactionsByUserWithinTimeWindow(@Param("userId") Long userId, @Param("dateTimeThreshold") LocalDateTime dateTimeThreshold);
}