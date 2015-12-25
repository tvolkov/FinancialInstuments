package com.infusion.calculation;

import com.infusion.correction.MultiplierProvider;

import java.util.Map;

public class Calculator {
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final MultiplierProvider multiplierProvider;

    public Calculator(Map<String, MeanCalculator> meanCalculatorMap, MultiplierProvider multiplierProvider){
        this.meanCalculatorMap = meanCalculatorMap;
        this.multiplierProvider = multiplierProvider;
    }

    public void calculate(Row row){
        if (meanCalculatorMap.containsKey(row.getInstrumentName())) {
            meanCalculatorMap.get(row.getInstrumentName()).increment(row.getDate(),
                    row.getPrice() * multiplierProvider.getMultiplierForInstrument(row.getInstrumentName()));
        }
    }
}
