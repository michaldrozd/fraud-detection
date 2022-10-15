package com.michaldrozd.frauddetection.services;

import com.michaldrozd.frauddetection.domain.Product;
import com.michaldrozd.frauddetection.domain.User;
import com.michaldrozd.frauddetection.repositories.ProductRepository;
import com.michaldrozd.frauddetection.repositories.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UserBehaviorAnalysisService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public UserBehaviorAnalysisService(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<User> findSimilarUsers(Long userId, int threshold) {
        return userRepository.findUsersWithSimilarPreferences(userId, threshold);
    }

    public List<Product> findPopularProducts(int popularityThreshold) {
        return productRepository.findPopularProducts(popularityThreshold);
    }

    public List<User> findSuspiciousActivities(LocalDateTime startDateTime, LocalDateTime endDateTime, int threshold) {
        return userRepository.findUsersWithAnomalies(startDateTime, endDateTime, threshold);
    }
}