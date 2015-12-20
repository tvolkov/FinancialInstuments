package com.infusion.calculation;

import com.infusion.correction.CorrectionProvider;

import java.util.Map;

public class Calculator {
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final CorrectionProvider correctionProvider;

    public Calculator(Map<String, MeanCalculator> meanCalculatorMap, CorrectionProvider correctionProvider){
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
    }

    public void calculate(Row row){
        if (meanCalculatorMap.containsKey(row.getInstrumentName())) {
            meanCalculatorMap.get(row.getInstrumentName()).increment(row.getDate(),
                    row.getPrice() * correctionProvider.getCorrectionForInstrument(row.getInstrumentName()));
        }
    }
}
