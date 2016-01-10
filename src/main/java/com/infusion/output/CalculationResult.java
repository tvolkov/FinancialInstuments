package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;

import java.util.Iterator;
import java.util.Map;

public class CalculationResult {
    private final Iterator<Map.Entry<String, CalculationStrategy>> iterator;
    private final int numberOfInstruments;
    private final long numberOfLinesProcessed;
    private final long totalExecutionTime;

    public CalculationResult(Iterator<Map.Entry<String, CalculationStrategy>> iterator, int numberOfInstruments, long numberOfLinesProcessed, long totalExecutionTime) {
        this.iterator = iterator;
        this.numberOfInstruments = numberOfInstruments;
        this.numberOfLinesProcessed = numberOfLinesProcessed;
        this.totalExecutionTime = totalExecutionTime;
    }

    public long getNumberOfLinesProcessed() {
        return numberOfLinesProcessed;
    }

    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public int getNumberOfInstruments() {
        return numberOfInstruments;
    }

    public Iterator<Map.Entry<String, CalculationStrategy>> getIterator() {
        return iterator;
    }
}
