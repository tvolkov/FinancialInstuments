package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;

import java.util.Iterator;
import java.util.Map;

public interface ResultWriter {
    void writeResults(Iterator<Map.Entry<String, CalculationStrategy>> iterator, int numberOfInstruments,
                      long numberOfLinesProcessed, long totalExecutionTime);
}
