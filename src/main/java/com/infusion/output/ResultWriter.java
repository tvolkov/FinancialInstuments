package com.infusion.output;

import com.infusion.calculation.ICalculationStrategyProvider;

public interface ResultWriter {
    void writeResults(ICalculationStrategyProvider calculationStrategyProvider, long numberOfLinesProcessed, long executionTime);
}
