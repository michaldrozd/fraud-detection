package com.michaldrozd.frauddetection.ai;

import java.net.URISyntaxException;
import org.apache.commons.csv.CSVFormat;
import smile.classification.LogisticRegression;
import smile.data.DataFrame;
import smile.io.Read;
import smile.math.MathEx;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * A class that trains a model for fraud detection.
 * TODO: in progress!
 */
public class FraudModelTrainer {
    public static LogisticRegression trainModel(String dataPath) throws IOException, URISyntaxException {
        DataFrame data = Read.csv(Paths.get(dataPath).toString(), CSVFormat.DEFAULT.withDelimiter(',').withFirstRecordAsHeader());
        // TODO: add column spec!

        int[] labels = data.column("is_fraud").toIntArray();
        double[][] features = data.drop("is_fraud").toArray();

        int n = features.length;
        int[] indices = MathEx.permutate(n);
        int trainSize = (int) (0.7 * n);

        double[][] trainFeatures = new double[trainSize][];
        int[] trainLabels = new int[trainSize];
        double[][] testFeatures = new double[n - trainSize][];
        int[] testLabels = new int[n - trainSize];

        for (int i = 0; i < trainSize; i++) {
            trainFeatures[i] = features[indices[i]];
            trainLabels[i] = labels[indices[i]];
        }

        for (int i = trainSize; i < n; i++) {
            testFeatures[i - trainSize] = features[indices[i]];
            testLabels[i - trainSize] = labels[indices[i]];
        }

        LogisticRegression model = LogisticRegression.fit(trainFeatures, trainLabels);

        int[] predictions = new int[testLabels.length];
        for (int i = 0; i < testLabels.length; i++) {
            predictions[i] = model.predict(testFeatures[i]);
        }

//        double accuracy = MathEx.accuracy(testLabels, predictions);
//        System.out.printf("Model accuracy: %.2f%%\n", 100.0 * accuracy);

        return model;
    }
}