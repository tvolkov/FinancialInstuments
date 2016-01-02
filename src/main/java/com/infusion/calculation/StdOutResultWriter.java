package com.infusion.calculation;

import java.util.Map;

public class StdOutResultWriter implements ResultWriter{

    public StdOutResultWriter(){

    }

    @Override
    public void writeResults(Map<String, MeanCalculator> meanCalculatorMap, long numberOfLinesProcessed, long totalExecutionTime) {
        System.out.println("Calculated values:");
        for (Map.Entry<String, MeanCalculator> entry : meanCalculatorMap.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());
        }
        System.out.println("------------------");
        System.out.println("Number of lines processed: " + numberOfLinesProcessed);
        System.out.println("------------------");
        System.out.println("Time elapsed: " + (totalExecutionTime > 1000 ? totalExecutionTime / 1000 + "s" : totalExecutionTime + "ms"));
    }
}
