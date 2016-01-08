package com.infusion.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import static com.infusion.calculation.InstrumentMetricsCalculationEngine.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private final Calculator calculator;
    private final BlockingQueue<String> queue;

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationWorker.class);

    private long numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> queue, Calculator calculator){
        this.queue = queue;
        this.calculator = calculator;
    }

    @Override
    public Long call() {
        LOGGER.debug("starting calculation worker thread");
        String line;
        try {
            while ((line = queue.take()) != null){
                if (line.equals(TERMINATING_ROW)){
                    LOGGER.debug("found terminating row, exiting");
                    break;
                }
                numberOfLinesProcessed++;
                calculator.calculate(line);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.debug("caught interruptedexception");
            return numberOfLinesProcessed;
        }

        LOGGER.debug(numberOfLinesProcessed + " lines processed");
        return numberOfLinesProcessed;
    }
}
