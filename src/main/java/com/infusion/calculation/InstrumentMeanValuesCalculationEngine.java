package com.infusion.calculation;

import com.infusion.correction.CorrectionProvider;
import com.infusion.reader.InputReader;
import com.infusion.reader.SingleThreadedInputReader;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class InstrumentMeanValuesCalculationEngine implements CalculationEngine {

    private final InputReader inputReader;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final CorrectionProvider correctionProvider;
    private final Set<Future<Long>> linesProcessed = new HashSet<>();
    private final Lock lock = new ReentrantLock();
    private final CountDownLatch countDownLatch = new CountDownLatch(DEFAULT_THREAD_POOL_SIZE);

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 500000;
    private static final int DEFAULT_THREAD_POOL_SIZE = 50;

    public InstrumentMeanValuesCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap,
                                                 CorrectionProvider correctionProvider){
        this.inputReader = new SingleThreadedInputReader(pathToFile, queue, lock, countDownLatch);
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
            linesProcessed.add(calculators.submit(new CalculationWorker(queue, meanCalculatorMap,
                correctionProvider, lock, countDownLatch)));
        }
        calculators.shutdown();
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
