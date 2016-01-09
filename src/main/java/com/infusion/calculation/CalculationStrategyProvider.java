package com.infusion.calculation;

import java.util.Iterator;
import java.util.Map;

class CalculationStrategyProvider implements ICalculationStrategyProvider {
    private final Map<String, CalculationStrategy> calculationStrategies;

    public CalculationStrategyProvider(Map<String, CalculationStrategy> calculationStrategies){
        this.calculationStrategies = calculationStrategies;
    }

    public CalculationStrategy getCalculationStrategy(String instrumentName){
            if (!calculationStrategies.containsKey(instrumentName)){
                calculationStrategies.put(instrumentName, new DefaultCalculationStrategy(10));
            }

            return calculationStrategies.get(instrumentName);
    }

    public Iterator<Map.Entry<String, CalculationStrategy>> getIterator(){
        return calculationStrategies.entrySet().iterator();
    }
}
