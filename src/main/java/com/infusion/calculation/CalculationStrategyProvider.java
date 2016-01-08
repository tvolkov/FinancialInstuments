package com.infusion.calculation;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class CalculationStrategyProvider implements ICalculationStrategyProvider {
    private final Map<String, CalculationStrategy> calculationStrategies;
//    private Lock lock = new ReentrantLock();

    public CalculationStrategyProvider(Map<String, CalculationStrategy> calculationStrategies){
        this.calculationStrategies = calculationStrategies;
    }

    public CalculationStrategy getCalculationStrategy(String instrumentName){
//        lock.lock();
//        try {
            if (!calculationStrategies.containsKey(instrumentName)){
                calculationStrategies.put(instrumentName, new DefaultCalculationStrategy(10));
            }

            return calculationStrategies.get(instrumentName);
//        } finally {
//            lock.unlock();
//        }

    }

    public Iterator<Map.Entry<String, CalculationStrategy>> getIterator(){
        return calculationStrategies.entrySet().iterator();
    }
}
