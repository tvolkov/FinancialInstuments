package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;
import com.infusion.calculation.ICalculationStrategyProvider;

import java.util.Iterator;
import java.util.Map;

public class StdOutResultWriter implements ResultWriter {

    public StdOutResultWriter(){
    }

    @Override
    public void writeResults(ICalculationStrategyProvider calculationStrategyProvider, long numberOfLinesProcessed, long totalExecutionTime) {
        System.out.println("Calculated values:");

        Iterator<Map.Entry<String, CalculationStrategy>> iterator = calculationStrategyProvider.getIterator();
        while (iterator.hasNext()){
            Map.Entry<String, CalculationStrategy> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());

        }
        System.out.println("------------------");
        System.out.println("Number of lines processed: " + numberOfLinesProcessed);
        System.out.println("------------------");
        System.out.println("Time elapsed: " + (totalExecutionTime > 1000 ? totalExecutionTime / 1000 + "s" : totalExecutionTime + "ms"));
    }
}
