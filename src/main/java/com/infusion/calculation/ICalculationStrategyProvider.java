package com.infusion.calculation;

import java.util.Iterator;
import java.util.Map;

public interface ICalculationStrategyProvider {
    CalculationStrategy getCalculationStrategy(String instrumentName);
    Iterator<Map.Entry<String, CalculationStrategy>> getIterator();
    int getNumberOfInstruments();
}
