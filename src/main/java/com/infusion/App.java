package com.infusion;

import com.infusion.calculation.InstrumentMeanValuesCalculationEngine;
import com.infusion.calculation.MeanCalculator;
import com.infusion.correction.CorrectionProvider;
import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main( String[] args ) {

        Map<String, MeanCalculator> meanCalculatorMap = Collections.unmodifiableMap(new HashMap<String, MeanCalculator>(){{
            put("INSTRUMENT1", new MeanCalculator());
            put("INSTRUMENT2", new MeanCalculator("Nov-2014"));
            put("INSTRUMENT3", new MeanCalculator("2014"));
        }});

        //TODO create real correction provider
//        CorrectionProvider correctionProvider = (String instrumentName) -> 1d;

        CorrectionProvider correctionProvider = new CorrectionProvider() {
            @Override
            public double getCorrectionForInstrument(String instrument) {
                return 1d;
            }
        };

        //todo use some DI framework to handle dependencies
        new InstrumentMeanValuesCalculationEngine("src/test/resources/example_input.txt",
            meanCalculatorMap, correctionProvider).calculateMetrics();
    }
}
