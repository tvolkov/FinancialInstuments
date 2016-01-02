package com.infusion.calculation;

import java.util.Map;

public interface ResultWriter {

    void writeResults(Map<String, MeanCalculator> meanCalculatorMap, long numberOfLinesProcessed, long executionTime);
}
