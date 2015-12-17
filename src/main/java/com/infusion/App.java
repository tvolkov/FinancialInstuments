package com.infusion;

import com.infusion.calculation.InstrumentMeanValuesCalculationEngine;
import com.infusion.calculation.MeanCalculator;
import com.infusion.correction.CorrectionProvider;
import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main( String[] args ) {
        Map<String, MeanCalculator> meanCalculatorMap = new HashMap<String, MeanCalculator>(){{
            put("INSTRUMENT1", new MeanCalculator());
            put("INSTRUMENT2", new MeanCalculator("Nov-2014"));
            put("INSTRUMENT3", new MeanCalculator("2014"));
        }};

        //TODO create real correction provider
        CorrectionProvider correctionProvider = (String instrumentName) -> {
            return 1d;
        };

        LineParser lineParser = new InstrumentLineParser();

        //todo use some DI framework to handle dependencies
        new InstrumentMeanValuesCalculationEngine("src/test/resources/large_file.txt",
            meanCalculatorMap, correctionProvider, lineParser).calculateMetrics();
    }
}
