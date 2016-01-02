package com.infusion.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private final Calculator calculator;
    private final BlockingQueue<String> queue;
    private final Lock lock;
    private final Condition notEmpty;

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationWorker.class);

    private long numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> queue, Calculator calculator, Lock lock, Condition notEmpty){
        this.queue = queue;
        this.calculator = calculator;
        this.lock = lock;
        this.notEmpty = notEmpty;
    }

    @Override
    public Long call() {
        String line;
        while (true){
            if(lock.tryLock()) {
                try {
                    /*
                     * we need to check if the queue is empty since reader thread should wait for all the calculatorworkers before sending TERMINATING_ROW.
                     * Otherwise some threads might wait forever to take an element from queue
                     */
                    while (queue.isEmpty()) {
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            return numberOfLinesProcessed;
                        }
                        LOGGER.debug("queue is empty, waiting");
                    }
                    line = queue.peek();
                    if (line != null && line.equals(TERMINATING_ROW)) {
                        LOGGER.debug("found terminating row, shutting down");
                        break;
                    } else {
                        line = queue.poll();
                        numberOfLinesProcessed++;
                        calculator.calculate(line);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        LOGGER.debug(numberOfLinesProcessed + " lines processed");
        return numberOfLinesProcessed;
    }
}
