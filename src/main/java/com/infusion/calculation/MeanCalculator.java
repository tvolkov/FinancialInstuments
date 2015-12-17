package com.infusion.calculation;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

/**
 * Created by tvolkov on 12/14/15.
 */
public class MeanCalculator {

    private Mean mean;
    //todo make it operate on dates instead of strings
    private String averagingPeriod;

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
            mean.increment(value);
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
