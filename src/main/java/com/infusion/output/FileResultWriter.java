package com.infusion.output;

import com.infusion.calculation.CalculationStrategy;

import java.util.Iterator;
import java.util.Map;

public abstract class FileResultWriter extends AbstractResultWriter {

    protected final String filePath;

    public FileResultWriter(String filePath){
        this.filePath = filePath;
    }
    @Override
    public abstract void writeResults(Iterator<Map.Entry<String, CalculationStrategy>> iterator,
                                      int numberOfInstruments, long numberOfLinesProcessed, long totalExecutionTime);
}
