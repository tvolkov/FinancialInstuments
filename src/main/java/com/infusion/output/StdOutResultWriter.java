package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;
import com.infusion.calculation.ICalculationStrategyProvider;

import java.util.Iterator;
import java.util.Map;

//todo create another resultwriters, like html, csv, or something else
public class StdOutResultWriter implements ResultWriter {

    public StdOutResultWriter(){
    }

    @Override
    public void writeResults(ICalculationStrategyProvider calculationStrategyProvider, long numberOfLinesProcessed, long totalExecutionTime) {
        System.out.println("Calculated values:");

        Iterator<Map.Entry<String, CalculationStrategy>> iterator = calculationStrategyProvider.getIterator();
        int size = 0;
        while (iterator.hasNext()){
            size++;
            Map.Entry<String, CalculationStrategy> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());

        }
        System.out.println("------------------");
        System.out.println("Total instruments: " + size);
        System.out.println("------------------");
        System.out.println("Number of lines processed: " + numberOfLinesProcessed);
        System.out.println("------------------");
        System.out.println("Time elapsed: " + calculateExecutionTime(totalExecutionTime));
    }

    private String calculateExecutionTime(long executionTimeLong){
        if (executionTimeLong < 1000){
            return executionTimeLong + "ms";
        } else if (executionTimeLong > 1000 && executionTimeLong < 60000){
            return executionTimeLong / 1000 + "s " + executionTimeLong % 1000 + "ms";
        } else {
            return executionTimeLong / 60000 + "m " + executionTimeLong % 60000 / 1000 + "s ";
        }
    }
}
