package com.infusion.calculation;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Calculates mean value for the given period
 */
public class MeanCalculationStrategy implements CalculationStrategy {

    private final Mean mean = new Mean();
    private final LocalDate averagingPeriodStart;
    private final LocalDate averagingPeriodEnd;
    private final Lock lock = new ReentrantLock();

    /**
     * Constructor to be used for calculating mean
     */
    public MeanCalculationStrategy(){
        this.averagingPeriodStart = null;
        this.averagingPeriodEnd = null;
    }

    /**
     * Constructor to be used for calculating mean for specified period
     */
    public MeanCalculationStrategy(LocalDate averagingPeriodStart, LocalDate averagingPeriodEnd){
        this.averagingPeriodStart = averagingPeriodStart;
        this.averagingPeriodEnd = averagingPeriodEnd;
    }


    @Override
    public void calculateInstrumentMetric(LocalDate date, double priceValue) {
        if (isDateWithinAveragingPeriod(date)){
            lock.lock();
            try {
                mean.increment(priceValue);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public double getResult() {
        return mean.getResult();
    }

    private boolean isDateWithinAveragingPeriod(LocalDate date){
        if (averagingPeriodStart == null && averagingPeriodEnd == null){
            return true;
        } else {
            return !(date.isBefore(averagingPeriodStart) || date.isAfter(averagingPeriodEnd));
        }
    }
}
