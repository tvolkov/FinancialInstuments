package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import com.infusion.reader.SingleThreadedInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InstrumentMeanValuesCalculationEngine implements CalculationEngine {

    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final MultiplierProvider multiplierProvider;
    private final Set<Future<Long>> linesProcessed = new HashSet<>();
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private final String pathToFile;
    private final ResultWriter resultWriter;

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 5000000;
    private static final int DEFAULT_THREAD_POOL_SIZE = 30;

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentMeanValuesCalculationEngine.class);

    public InstrumentMeanValuesCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap,
                                                 MultiplierProvider multiplierProvider, ResultWriter resultWriter){
        this.pathToFile = pathToFile;
        this.meanCalculatorMap = meanCalculatorMap;
        this.multiplierProvider = multiplierProvider;
        this.resultWriter = resultWriter;
    }

    @Override
    public void calculateMetrics() {   
        long startTime = System.currentTimeMillis();
        ExecutorService reader = Executors.newSingleThreadExecutor();
        LOGGER.info("Starting reader thread");
        reader.submit(new SingleThreadedInputReader(pathToFile, queue, lock, notEmpty));
        reader.shutdown();

        ExecutorService calculators = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        for (int i = 0; i < DEFAULT_THREAD_POOL_SIZE; i++){
            LOGGER.info("Starting new calculator thread");
            linesProcessed.add(calculators.submit(new CalculationWorker(queue, new Calculator(meanCalculatorMap,
                    multiplierProvider, new InstrumentLineParser()), lock, notEmpty)));
        }
        calculators.shutdown();
        LOGGER.info("waiting for all the workers to finish");
        try {
            calculators.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        LOGGER.info("getting results");
        for (Future<Long> future : linesProcessed){
            try {
                numberOfLinesProcessed += future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }
        long endTime = System.currentTimeMillis();

        this.totalExecutionTime = endTime - startTime;
        resultWriter.writeResults(meanCalculatorMap, numberOfLinesProcessed, totalExecutionTime);
        LOGGER.info("done");
    }
}
