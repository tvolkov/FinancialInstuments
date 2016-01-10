package com.infusion.calculation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import com.infusion.output.CalculationResult;
import com.infusion.output.ResultWriter;
import com.infusion.reader.FileInputReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericGroovyApplicationContext;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class InstrumentMetricsCalculationEngine implements CalculationEngine {

    private final CalculationStrategyProvider calculationStrategyProvider;
    private final String pathToFile;
    private final ResultWriter[] resultWriters;
    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
    private final Set<Future<Long>> linesProcessed = new HashSet<>();

    private static final int DEFAULT_QUEUE_CAPACITY = 5000000;
    private static final int DEFAULT_THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final Logger LOGGER = LoggerFactory.getLogger(InstrumentMetricsCalculationEngine.class);

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public InstrumentMetricsCalculationEngine(String pathToFile, CalculationStrategyProvider calculationStrategyProvider,
                                              ResultWriter... resultWriter) {
        this.pathToFile = pathToFile;
        this.calculationStrategyProvider = calculationStrategyProvider;
        this.resultWriters = resultWriter;
    }

    @Override
    public void calculateMetrics() {
        long startTime = System.currentTimeMillis();

        GenericGroovyApplicationContext ctx = new GenericGroovyApplicationContext("classpath:beans.groovy");

        //start input reader thread
        CountDownLatch countDownLatch = new CountDownLatch(1);
        startReaderThread(countDownLatch);

        //start calculator threads
        ExecutorService calculators = startCalculatorThreads(ctx);

        //wait while reader is done
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("Error occurred", e);
            throw new RuntimeException(e);
        }

        //tell calculators to stop
        terminateCalculators(calculators);

        long endTime = System.currentTimeMillis();

        long totalExecutionTime = endTime - startTime;

        //obtaining results
        for (ResultWriter resultWriter : resultWriters){
            resultWriter.writeResults(new CalculationResult(calculationStrategyProvider.getIterator(),
                    calculationStrategyProvider.getNumberOfInstruments(), countLines(), totalExecutionTime));
        }
        LOGGER.info("done");
    }

    private long countLines(){
        long numberOfLinesProcessed = 0;
        for (Future<Long> future : linesProcessed) {
            try {
                numberOfLinesProcessed += future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
        }
        return numberOfLinesProcessed;
    }

    private Calculator createNewCalculator(GenericGroovyApplicationContext ctx) {
        return new Calculator(
                calculationStrategyProvider,
                (MultiplierProvider) ctx.getBean("multiplierProvider"),
                (InstrumentLineParser) ctx.getBean("instrumentLineParser"),
                (DateValidator) ctx.getBean("dateValidator"));
    }

    private void startReaderThread(CountDownLatch countDownLatch){
        ExecutorService reader = Executors.newSingleThreadExecutor();
        LOGGER.info("Starting reader thread with file " + pathToFile);
        reader.submit(new FileInputReader(new File(pathToFile), queue, countDownLatch));
        reader.shutdown();
    }

    private ExecutorService startCalculatorThreads(GenericGroovyApplicationContext ctx){
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("CalculationWorker-%d").build();
        ExecutorService calculators = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE, threadFactory);

        for (int i = 0; i < DEFAULT_THREAD_POOL_SIZE; i++) {
            linesProcessed.add(calculators.submit(new CalculationWorker(queue, createNewCalculator(ctx))));
        }
        calculators.shutdown();
        return calculators;
    }

    private void terminateCalculators(ExecutorService calculators){
        int count = 0;
        while (!calculators.isTerminated() && count++ < DEFAULT_THREAD_POOL_SIZE) {
            LOGGER.debug("putting terminating row into queue");
            queue.offer(TERMINATING_ROW);
        }
    }
}
