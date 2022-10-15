# Spring Boot Fraud Detection with Neo4j

This project is a Spring Boot application that demonstrates a fraud detection system using Neo4j graph database.
It showcases how to model entities and relationships for fraud detection and efficiently query the graph database to identify potential fraudulent transactions.

## Features

- Model entities and relationships for User, Transaction, CreditCard, Device, and Merchant.
- Store and retrieve data from a Neo4j graph database.
- Analyze transactions within a given time window to identify potential fraud based on factors such as:
   - Number of transactions exceeding a threshold.
   - Total transaction amount exceeding an amount threshold.
   - Usage of multiple devices or credit cards for transactions within the time window.
   - Location-based fraud by calculating the distance between transaction locations...
   - IN PROGRESS: UserBehaviorAnalysisService class contains the logic for analyzing transactions and identifying potential fraud.
- Expose a REST API endpoint to check if a User's transactions within a specified time window are potentially fraudulent.

**TODO - Machine learning-based fraud detection - FraudModelTrainer**

Train a machine learning model using historical transaction data to predict the likelihood of a transaction being fraudulent.

## Prerequisites

- JDK 16 or later
- Neo4j Database (local or remote)
- Gradle (wrapper included)

## Getting Started

1. Clone the repository:

```bash
git clone https://github.com/michaldrozd/fraud-detection.git
```

2. Navigate to the project directory:
```bash
cd fraud-detection
```

3. Update the src/main/resources/application.properties file with your Neo4j database credentials and settings:
```properties
spring.neo4j.uri=bolt://localhost:7687
spring.neo4j.authentication.username=neo4j
spring.neo4j.authentication.password=your_password
```

4. Build the project using Gradle:
```bash
./gradlew build
```

5. Run the Spring Boot application:

To run neo4j locally, you can use the following docker command:
```bash
docker run --publish=7474:7474 --publish=7687:7687 --env=NEO4J_AUTH=none neo4j
```
Then run the Spring Boot application:
```bash
./gradlew bootRun
```

6. Test the REST API endpoint to check if a User's transactions within a specified time window are potentially fraudulent:
```bash
curl -X GET 'http://localhost:8080/users/{userId}/fraud-check?transactionsThreshold=5&timeWindowInMinutes=60&amountThreshold=2000'
```

Replace {userId} with the actual user ID.
