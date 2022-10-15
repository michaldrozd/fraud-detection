package com.michaldrozd.frauddetection.services;

import com.michaldrozd.frauddetection.ai.FraudModelTrainer;
import com.michaldrozd.frauddetection.domain.Device;
import com.michaldrozd.frauddetection.domain.Transaction;
import com.michaldrozd.frauddetection.domain.User;
import com.michaldrozd.frauddetection.repositories.TransactionRepository;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smile.classification.LogisticRegression;

@Slf4j
@Service
public class FraudDetectionService {

    private final TransactionRepository transactionRepository;
    private LogisticRegression model;

    @Autowired
    public FraudDetectionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        // TODO: Implement the model training like this
//        try {
//            this.model = FraudModelTrainer.trainModel(modelDataPath);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to train fraud detection model", e);
//        }
    }

    /**
     * Determines whether a User's transactions within a given time window are potentially fraudulent.
     * <p>
     * The method takes four parameters: a User object, a threshold for the number of transactions, a time window in minutes,
     * and an amount threshold for total transaction amount. It analyzes the User's transactions within the specified time
     * window to identify potential fraud based on multiple factors, such as:
     * 1. Number of transactions exceeding the threshold.
     * 2. Total transaction amount exceeding the amount threshold.
     * 3. Usage of multiple devices or credit cards for transactions within the time window.
     * 4. Location of the transactions exceeding the distance threshold.
     * 5. AI-based analysis of the transactions???
     * <p>
     *
     * @param user                  The User for which to analyze the transactions.
     * @param transactionsThreshold The maximum number of transactions allowed within the time window.
     * @param timeWindowInMinutes   The time window in minutes for filtering transactions.
     * @param amountThreshold       The maximum total transaction amount allowed within the time window.
     * @param distanceThresholdInKm The maximum distance between two consecutive transactions within the time window.
     *
     * @return A boolean value indicating whether the User's transactions within the time window are potentially fraudulent.
     */
    public boolean isFraudulent(User user, int transactionsThreshold, int timeWindowInMinutes, double amountThreshold, int distanceThresholdInKm) {
        List<Transaction> recentTransactions = transactionRepository.findTransactionsByUserWithinTimeWindow(
            user.getId(),
            LocalDateTime.now().minusMinutes(timeWindowInMinutes)
        );

        // Check if the number of transactions within the time window exceeds the threshold
        if (recentTransactions.size() >= transactionsThreshold) {
            log.info("User {} has exceeded the number of transactions within the time window.", user.getId());
            return true;
        }

        // Check for unusually high transaction amounts
        double totalAmount = recentTransactions.stream()
                                               .mapToDouble(Transaction::getAmount)
                                               .sum();

        if (totalAmount >= amountThreshold) {
            log.info("User {} has exceeded the amount threshold within the time window.", user.getId());
            return true;
        }

        // Check for transactions with multiple devices or credit cards in a short time window
        long distinctDevices = recentTransactions.stream()
                                                 .map(transaction -> transaction.getDevice().getId())
                                                 .distinct()
                                                 .count();

        long distinctCreditCards = recentTransactions.stream()
                                                     .map(transaction -> transaction.getCreditCard().getId())
                                                     .distinct()
                                                     .count();

        if (distinctDevices > 1 || distinctCreditCards > 1) {
            log.info("User {} has used multiple devices or credit cards within the time window.", user.getId());
            return true;
        }

        // Check for location-based fraud
        return isLocationBasedFraud(recentTransactions, distanceThresholdInKm, timeWindowInMinutes);
    }


    /**
     * Checks for location-based fraud by calculating the distance between transaction locations within the specified time window.
     * <p>
     * This method takes a list of recent transactions, a distance threshold (in kilometers), and a time window (in minutes) as input parameters.
     * It calculates the distance between each pair of consecutive transactions and checks if any of these distances exceed the specified threshold.
     * If any distance exceeds the threshold, the method returns true, indicating potential location-based fraud.
     *
     * @param recentTransactions   The list of recent transactions for a specific user.
     * @param distanceThresholdInKm The maximum allowed distance between two consecutive transactions within the time window.
     * @param timeWindowInMinutes  The time window in minutes within which transactions are considered.
     *
     * @return true if any distance between consecutive transactions exceeds the threshold, indicating potential location-based fraud; false otherwise.
     */
    private boolean isLocationBasedFraud(List<Transaction> recentTransactions, int distanceThresholdInKm, int timeWindowInMinutes) {
        for (int i = 0; i < recentTransactions.size(); i++) {
            Transaction transaction1 = recentTransactions.get(i);
            Device device1 = transaction1.getDevice();
            // Compare each transaction with all subsequent transactions within the time window
            for (int j = i + 1; j < recentTransactions.size(); j++) {
                Transaction transaction2 = recentTransactions.get(j);
                Device device2 = transaction2.getDevice();
                // Check if the time difference between the two transactions is within the time window
                if (Math.abs(Duration.between(transaction1.getTransactionDate(), transaction2.getTransactionDate()).toMinutes()) <= timeWindowInMinutes) {
                    // Calculate the distance between the two transactions
                    double distance = calculateDistance(device1.getLatitude(), device1.getLongitude(), device2.getLatitude(), device2.getLongitude());
                    if (distance >= distanceThresholdInKm) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Calculates the distance between two geographical coordinates (latitude and longitude) using the Haversine formula.
     * <p>
     * This method takes two sets of coordinates (latitude1, longitude1) and (latitude2, longitude2) as input parameters.
     * It calculates the distance between these coordinates on the Earth's surface in kilometers, using the Haversine formula.
     * The method returns the calculated distance as a double value.
     *
     * @param latitude1  The latitude of the first coordinate in decimal degrees.
     * @param longitude1 The longitude of the first coordinate in decimal degrees.
     * @param latitude2  The latitude of the second coordinate in decimal degrees.
     * @param longitude2 The longitude of the second coordinate in decimal degrees.
     *
     * @return The distance between the two coordinates in kilometers.
     */
    private double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(latitude2 - latitude1);
        double lonDistance = Math.toRadians(longitude2 - longitude1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}