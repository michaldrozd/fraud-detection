package com.michaldrozd.frauddetection.controllers;

import com.michaldrozd.frauddetection.domain.User;
import com.michaldrozd.frauddetection.repositories.UserRepository;
import com.michaldrozd.frauddetection.services.FraudDetectionService;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Check fraud API.
 */
@RestController
@RequestMapping("/users/{userId}/fraud-check")
public class FraudDetectionController {

    private final FraudDetectionService fraudDetectionService;
    private final UserRepository userRepository;

    public FraudDetectionController(FraudDetectionService fraudDetectionService, UserRepository userRepository) {
        this.fraudDetectionService = fraudDetectionService;
        this.userRepository = userRepository;
    }

    /**
     * Checks if a user is fraudulent.
     *
     * @param userId                user id
     * @param transactionsThreshold number of transactions threshold
     * @param timeWindowInMinutes   time window in minutes
     * @param amountThreshold       amount threshold
     * @param distanceThresholdInKm distance threshold in km
     *
     * @return response entity with isFraudulent flag
     */
    @GetMapping
    public ResponseEntity<?> checkFraud(@PathVariable Long userId,
                                        @RequestParam int transactionsThreshold,
                                        @RequestParam int timeWindowInMinutes,
                                        @RequestParam double amountThreshold,
                                        @RequestParam int distanceThresholdInKm) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        boolean isFraudulent = fraudDetectionService.isFraudulent(user, transactionsThreshold, timeWindowInMinutes, amountThreshold, distanceThresholdInKm);

        // TODO: return some more meaningful response
        return ResponseEntity.ok().body(Map.of("isFraudulent", isFraudulent));
    }
}