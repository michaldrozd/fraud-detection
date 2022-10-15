package com.michaldrozd.frauddetection.repositories;

import com.michaldrozd.frauddetection.domain.Product;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends Neo4jRepository<Product, Long> {

    /**
     * Finds and returns a list of Product objects that belong to a specific category.
     * <p>
     * The method takes one parameter: the category for which to retrieve the products.
     *
     * @param category The category for which to retrieve the products.
     * @return A list of Product objects belonging to the specified category.
     */
    @Query("MATCH (p:Product) WHERE p.category = $category RETURN p")
    List<Product> findProductsByCategory(@Param("category") String category);

    /**
     * Finds and returns a list of Product objects that are frequently viewed together with the product with the given productId.
     * <p>
     * The method takes two parameters: the Product's ID and a threshold for the minimum number of shared views required
     * for a product to be considered frequently viewed together.
     *
     * @param productId The ID of the Product for which to retrieve frequently viewed products.
     * @param threshold The minimum number of shared views required for a product to be considered frequently viewed together.
     * @return A list of Product objects frequently viewed together with the specified product.
     */
    @Query("MATCH (p:Product)<-[:VIEWED]-(u:User)-[:VIEWED]->(otherProduct:Product) WHERE p.id = $productId AND id(otherProduct) <> id(p) WITH otherProduct, count(u) AS sharedViews WHERE sharedViews >= $threshold RETURN otherProduct")
    List<Product> findFrequentlyViewedTogetherProducts(@Param("productId") Long productId, @Param("threshold") int threshold);

    /**
     * Finds and returns a list of popular Product objects based on a given popularity threshold.
     * <p>
     * The method takes one parameter: the minimum number of views required for a product to be considered popular.
     *
     * @param popularityThreshold The minimum number of views required for a product to be considered popular.
     * @return A list of popular Product objects.
     */
    @Query("MATCH (p:Product)<-[:VIEWED]-(u:User) WITH p, count(u) as views WHERE views >= $popularityThreshold RETURN p ORDER BY views DESC")
    List<Product> findPopularProducts(@Param("popularityThreshold") int popularityThreshold);

}