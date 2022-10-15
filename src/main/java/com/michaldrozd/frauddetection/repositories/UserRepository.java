package com.michaldrozd.frauddetection.repositories;

import com.michaldrozd.frauddetection.domain.Product;
import com.michaldrozd.frauddetection.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends Neo4jRepository<User, Long> {

    /**
     * Finds and returns a list of User objects who have liked a similar set of products as the user with the given userId.
     * <p>
     * The method takes two parameters: the User's ID and a threshold for the minimum number of shared preferences required
     * for a user to be considered similar.
     *
     * @param userId    The ID of the User for which to retrieve similar users.
     * @param threshold The minimum number of shared preferences required for a user to be considered similar.
     *
     * @return A list of User objects with similar product preferences as the specified user.
     */
    @Query("MATCH (u:User)-[:LIKES]->(p:Product)<-[:LIKES]-(otherUser:User) WHERE u.id = $userId AND id(otherUser) <> id(u) WITH otherUser, count(p) AS sharedPreferences WHERE sharedPreferences >= $threshold RETURN otherUser")
    List<User> findUsersWithSimilarPreferences(@Param("userId") Long userId, @Param("threshold") int threshold);


    /**
     * Finds and returns a list of Product objects that have been viewed by the user with the given userId.
     * <p>
     * The method takes one parameter: the User's ID for which to retrieve the viewed products.
     *
     * @param userId The ID of the User for which to retrieve the viewed products.
     *
     * @return A list of Product objects viewed by the specified user.
     */
    @Query("MATCH (u:User)-[:VIEWED]->(p:Product) WHERE u.id = $userId RETURN p")
    List<Product> findProductsViewedByUser(@Param("userId") Long userId);

    /**
     * Finds and returns a list of User objects with an unusually high number of transactions within a specified time range.
     * <p>
     * The method takes three parameters: a start LocalDateTime, an end LocalDateTime, and a threshold for the minimum number
     * of transactions required for a user to be considered as having anomalies.
     *
     * @param startDateTime The start LocalDateTime for the time range to consider.
     * @param endDateTime   The end LocalDateTime for the time range to consider.
     * @param threshold     The minimum number of transactions required for a user to be considered as having anomalies.
     *
     * @return A list of User objects with an unusually high number of transactions within the specified time range.
     */
    @Query("MATCH (u:User)-[:PERFORMED_BY]->(t:Transaction) WHERE t.transactionDate >= $startDateTime AND t.transactionDate <= $endDateTime WITH u, count(t) AS transactionCount WHERE transactionCount >= $threshold RETURN u")
    List<User> findUsersWithAnomalies(@Param("startDateTime") LocalDateTime startDateTime, @Param("endDateTime") LocalDateTime endDateTime, @Param("threshold") int threshold);

}