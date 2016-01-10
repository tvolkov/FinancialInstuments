package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;

import java.util.Iterator;
import java.util.Map;

public class StdOutResultWriter extends AbstractResultWriter {

    @Override
    public void writeResults(Iterator<Map.Entry<String, CalculationStrategy>> iterator, int numberOfInstruments, long numberOfLinesProcessed, long totalExecutionTime) {
        final String lineSeparator = System.lineSeparator();
        StringBuilder stringBuilder = new StringBuilder("Calculated values:");
        iterator.forEachRemaining(entry -> stringBuilder.append(lineSeparator).append(entry.getKey()).append(": ")
                .append(entry.getValue().getResult()).append(lineSeparator));

        stringBuilder.append(lineSeparator).append("------------------").append(lineSeparator)
                .append("Total instruments: ").append(numberOfInstruments).append(lineSeparator)
                .append("------------------").append(lineSeparator)
                .append("Number of lines processed: ").append(numberOfLinesProcessed).append(lineSeparator)
                .append("------------------").append(lineSeparator)
                .append("Time elapsed: ").append(calculateExecutionTime(totalExecutionTime))
                .append(lineSeparator);
        System.out.println(stringBuilder.toString());
    }
}
