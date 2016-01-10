package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;

import java.util.Iterator;
import java.util.Map;

public abstract class AbstractResultWriter implements ResultWriter {

    @Override
    public abstract void writeResults(Iterator<Map.Entry<String, CalculationStrategy>> iterator,
                                      int numberOfInstruments, long numberOfLinesProcessed, long totalExecutionTime);

    protected String calculateExecutionTime(long executionTimeLong){
        if (executionTimeLong < 1000){
            return executionTimeLong + "ms";
        } else if (executionTimeLong > 1000 && executionTimeLong < 60000){
            return executionTimeLong / 1000 + "s " + executionTimeLong % 1000 + "ms";
        } else {
            return executionTimeLong / 60000 + "m " + executionTimeLong % 60000 / 1000 + "s ";
        }
    }
}
