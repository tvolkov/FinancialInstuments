package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;

import java.util.Map;

public class Calculator {
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final MultiplierProvider multiplierProvider;
    private final InstrumentLineParser instrumentLineParser;

    public Calculator(Map<String, MeanCalculator> meanCalculatorMap, MultiplierProvider multiplierProvider, InstrumentLineParser instrumentLineParser){
        this.meanCalculatorMap = meanCalculatorMap;
        this.multiplierProvider = multiplierProvider;
        this.instrumentLineParser = instrumentLineParser;
    }

    public void calculate(String line){
        Row row = instrumentLineParser.parseLine(line);
        if (meanCalculatorMap.containsKey(row.getInstrumentName())) {
            meanCalculatorMap.get(row.getInstrumentName()).increment(row.getDate(),
                    row.getPrice() * multiplierProvider.getMultiplierForInstrument(row.getInstrumentName()));
        }
    }
}
