package com.infusion;

import com.infusion.correction.CorrectionProvider;

import java.util.HashMap;
import java.util.Map;

public class App
{
    public static void main( String[] args ) {

        Map<String, MeanCalculator> meanCalculatorMap = new HashMap<String, MeanCalculator>(){{
            put("INSTRUMENT1", new MeanCalculator());
            put("INSTRUMENT2", new MeanCalculator("Nov-2014"));
            put("INSTRUMENT3", new MeanCalculator("2014"));
        }};

        CorrectionProvider correctionProvider = new CorrectionProvider() {
            @Override
            public double getCorrectionForInstrument(String instrument) {
                return 1d;
            }
        };
        new InstrumentMetricCalculationEngine("src/test/resources/example_input.txt", meanCalculatorMap, correctionProvider).calculateMetrics();
    }
}
