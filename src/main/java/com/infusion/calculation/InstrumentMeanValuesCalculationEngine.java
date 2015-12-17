package com.infusion.calculation;

import com.infusion.correction.CorrectionProvider;
import com.infusion.reader.InputReader;
import com.infusion.reader.SingleThreadedInputReader;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class InstrumentMeanValuesCalculationEngine implements CalculationEngine {

    private final InputReader inputReader;
    private final BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final CorrectionProvider correctionProvider;
    private final Set<Future<Long>> linesProcessed = new HashSet<>();

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 100000;
    private static final int DEFAULT_THREAD_POOL_SIZE = 2;

    public InstrumentMeanValuesCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap,
                                                 CorrectionProvider correctionProvider){
        this.inputReader = new SingleThreadedInputReader(pathToFile, blockingQueue);
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
    }

    @Override
    public void calculateMetrics() {   
        long startTime = System.currentTimeMillis();
        ExecutorService reader = Executors.newSingleThreadExecutor();
        reader.submit(inputReader);
        reader.shutdown();

        ExecutorService calculators = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        for (int i = 0; i < DEFAULT_THREAD_POOL_SIZE; i++){
            System.out.println("Starting new calculator thread");
            linesProcessed.add(calculators.submit(new CalculationWorker(blockingQueue, meanCalculatorMap,
                    correctionProvider)));
        }
        calculators.shutdown();
        System.out.println("shutdown, await termination");
        try {
            calculators.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

        long endTime = System.currentTimeMillis();

        for (Future<Long> future : linesProcessed){
            try {
                numberOfLinesProcessed += future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        this.totalExecutionTime = endTime - startTime;

        printInfo();

    }

    //TODO create interface like MetricsPrinter to allow output to different places
    private void printInfo() {
        System.out.println("Calculated values:");
        for (Map.Entry<String, MeanCalculator> entry : meanCalculatorMap.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());
        }
        System.out.println("------------------");
        System.out.println("Number of lines processed: " + numberOfLinesProcessed);
        System.out.println("------------------");
        System.out.println("Time elapsed: " + (totalExecutionTime > 1000 ? totalExecutionTime / 1000 + "s" : totalExecutionTime + "ms"));
    }
}
