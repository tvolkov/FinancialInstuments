package com.infusion.calculation;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.time.LocalDate;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MeanCalculator {

    private final Mean mean;
    private final LocalDate averagingPeriodStart;
    private final LocalDate averagingPeriodEnd;
    private final Lock lock = new ReentrantLock();

    public MeanCalculator(){
        this.mean = new Mean();
        this.averagingPeriodStart = null;
        this.averagingPeriodEnd = null;
    }

    public MeanCalculator(Mean mean, LocalDate averagingPeriodStart, LocalDate averagingPeriodEnd){
        this.mean = mean;
        this.averagingPeriodStart = averagingPeriodStart;
        this.averagingPeriodEnd = averagingPeriodEnd;
    }

    public void increment(LocalDate date, double value){
        if (isDateWithinAveragingPeriod(date)){
            lock.lock();
            try {
                mean.increment(value);
            } finally {
                lock.unlock();
            }
        }
    }

    public double getResult(){
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
