package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import com.infusion.reader.FileInputReader;
import com.infusion.reader.InputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    private final String pathToFile;
    private final ResultWriter resultWriter;

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 5000000;
    private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentMeanValuesCalculationEngine.class);

    public InstrumentMeanValuesCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap,
                                                 MultiplierProvider multiplierProvider, ResultWriter resultWriter) {
        this.pathToFile = pathToFile;
        this.meanCalculatorMap = meanCalculatorMap;
        this.multiplierProvider = multiplierProvider;
        this.resultWriter = resultWriter;
    }

    @Override
    public void calculateMetrics() {
        long startTime = System.currentTimeMillis();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ExecutorService reader = Executors.newSingleThreadExecutor();
        LOGGER.info("Starting reader thread with file " + pathToFile);
        reader.submit(new FileInputReader(new File(pathToFile), queue, countDownLatch));
        reader.shutdown();

        ExecutorService calculators = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
        for (int i = 0; i < DEFAULT_THREAD_POOL_SIZE; i++) {
            linesProcessed.add(calculators.submit(new CalculationWorker(queue, new Calculator(meanCalculatorMap,
                    multiplierProvider, new InstrumentLineParser()))));
        }
        calculators.shutdown();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int count = 0;
        while (!calculators.isTerminated() && count++ < DEFAULT_THREAD_POOL_SIZE) {
            LOGGER.debug("putting terminating row into queue");
            queue.offer(TERMINATING_ROW);
        }

        LOGGER.info("getting results");
        for (Future<Long> future : linesProcessed) {
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
