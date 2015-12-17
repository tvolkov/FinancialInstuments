package com.infusion.calculation;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MeanCalculator {

    private final Mean mean;
    //todo make it operate on dates instead of strings
    private final String averagingPeriod;
    private final Lock lock = new ReentrantLock();

    public MeanCalculator(){
        this.mean = new Mean();
        this.averagingPeriod = "";
    }

    public MeanCalculator(String averagingPeriod){
        this.mean = new Mean();
        this.averagingPeriod = averagingPeriod;
    }

    public void increment(String date, double value){
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

    private boolean isDateWithinAveragingPeriod(String date){
        if (averagingPeriod.isEmpty()){
            return true;
        } else {
            //TODO add comparison here
            return true;
        }
    }
}
