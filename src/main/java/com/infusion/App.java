package com.infusion;

import com.infusion.correction.CorrectionProvider;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static final int DEFAULT_QUEUE_CAPACITY = 10000;

    public static void main( String[] args ) {

        Map<String, MeanCalculator> meanCalculatorMap = new HashMap<String, MeanCalculator>(){{
            put("INSTRUMENT1", new MeanCalculator());
            put("INSTRUMENT2", new MeanCalculator("Nov-2014"));
            put("INSTRUMENT3", new MeanCalculator("2014"));
        }};
        //TODO create real correction provider
        CorrectionProvider correctionProvider = new CorrectionProvider() {
            @Override
            public double getCorrectionForInstrument(String instrument) {
                return 1d;
            }
        };
        new InstrumentMeanValuesCalculationEngine("src/test/resources/example_input.txt",
            meanCalculatorMap, correctionProvider, DEFAULT_QUEUE_CAPACITY).calculateMetrics();
    }
}
