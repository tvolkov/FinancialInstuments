package com.infusion.calculation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import com.infusion.output.ResultWriter;
import com.infusion.reader.FileInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

    public class InstrumentMetricsCalculationEngine implements CalculationEngine {

    private final CalculationStrategyProvider calculationStrategyProvider;
    private final MultiplierProvider multiplierProvider;
    private final Set<Future<Long>> linesProcessed = new HashSet<>();
    private final String pathToFile;
    private final ResultWriter resultWriter;

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 5000000;
    private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentMetricsCalculationEngine.class);

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public InstrumentMetricsCalculationEngine(String pathToFile, CalculationStrategyProvider calculationStrategyProvider,
                                              MultiplierProvider multiplierProvider, ResultWriter resultWriter) {
        this.pathToFile = pathToFile;
        this.calculationStrategyProvider = calculationStrategyProvider;
        this.multiplierProvider = multiplierProvider;
        this.resultWriter = resultWriter;
    }

    @Override
    public void calculateMetrics() {
        long startTime = System.currentTimeMillis();

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);

        //start input reader thread
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //todo use this thread pool for calculation workers as well
        ExecutorService reader = Executors.newSingleThreadExecutor();
        LOGGER.info("Starting reader thread with file " + pathToFile);
        reader.submit(new FileInputReader(new File(pathToFile), queue, countDownLatch));
        reader.shutdown();

        //start calculator threads
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("CalculationWorker-%d").build();
        ExecutorService calculators = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE, threadFactory);
        for (int i = 0; i < DEFAULT_THREAD_POOL_SIZE; i++) {
            linesProcessed.add(calculators.submit(new CalculationWorker(queue, new Calculator(calculationStrategyProvider,
                    multiplierProvider, new InstrumentLineParser()))));
        }
        calculators.shutdown();

        //wait while reader is done
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //tell calculators to stop
        int count = 0;
        while (!calculators.isTerminated() && count++ < DEFAULT_THREAD_POOL_SIZE) {
            LOGGER.debug("putting terminating row into queue");
            queue.offer(TERMINATING_ROW);
        }

        //get results
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
        resultWriter.writeResults(calculationStrategyProvider, numberOfLinesProcessed, totalExecutionTime);
        LOGGER.info("done");
    }
}
