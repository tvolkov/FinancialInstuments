package com.infusion.calculation;

import java.time.LocalDate;
import java.util.*;

/**
 * Calculates sum of the 10 newset values
 */
public class DefaultCalculationStrategy implements CalculationStrategy {

    private final int numberOfElementsToSum;
    private TreeMap<LocalDate, Double> latestDates = new TreeMap<>();

    public DefaultCalculationStrategy(int numberOfElementsToSum){
        this.numberOfElementsToSum = numberOfElementsToSum;
    }

    @Override
    public void calculateInstrumentMetric(LocalDate date, double priceValue) {
            if (latestDates.size() == numberOfElementsToSum){
                LocalDate firstKey = latestDates.firstKey();
                if (firstKey.compareTo(date) < 0){
                    latestDates.remove(firstKey);
                    latestDates.put(date, priceValue);
                }
            } else {
                latestDates.put(date, priceValue);
            }
    }

    @Override
    public double getResult() {
        return latestDates.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
