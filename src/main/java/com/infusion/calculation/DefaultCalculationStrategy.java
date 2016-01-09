package com.infusion.calculation;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Calculates sum of the 10 newset values
 */
public class DefaultCalculationStrategy implements CalculationStrategy {

    private final int numberOfElementsToSum;
    private TreeMap<LocalDate, Double> latestDates = new TreeMap<>();
    private final Lock lock = new ReentrantLock();

    public DefaultCalculationStrategy(int numberOfElementsToSum){
        this.numberOfElementsToSum = numberOfElementsToSum;
    }

    @Override
    public void calculateInstrumentMetric(LocalDate date, double priceValue) {
        lock.lock();
        try {
            if (latestDates.size() == numberOfElementsToSum) {
                LocalDate firstKey = latestDates.firstKey();
                if (firstKey.compareTo(date) < 0) {
                    latestDates.remove(firstKey);
                    latestDates.put(date, priceValue);
                }
            } else {
                latestDates.put(date, priceValue);
            }
        }finally {
            lock.unlock();
        }
    }

    @Override
    public double getResult() {
        return latestDates.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
