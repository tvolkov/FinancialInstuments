package com.infusion.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static com.infusion.calculation.InstrumentMeanValuesCalculationEngine.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private final Calculator calculator;
//    private final ConcurrentLinkedQueue<String> queue;
    private final BlockingQueue<String> queue;
//    private final Lock lock;
//    private final Condition notEmpty;

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationWorker.class);

    private long numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> queue, Calculator calculator/*, Lock lock, Condition notEmpty*/){
        this.queue = queue;
        this.calculator = calculator;
//        this.lock = lock;
//        this.notEmpty = notEmpty;
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
            throw new RuntimeException(e);
        }
//        while (true){
//            if(lock.tryLock()) {
//                try {
                    /*
                     * we need to check if the queue is empty since reader thread should wait for all the calculatorworkers before sending TERMINATING_ROW.
                     * Otherwise some threads might wait forever to take an element from queue
                     */
//                    while (queue.isEmpty()) {
//                        try {
//                            notEmpty.await();
//                        } catch (InterruptedException e) {
//                            return numberOfLinesProcessed;
//                        }
//                        LOGGER.debug("queue is empty, waiting");
//                    }

/*                    if (queue.isEmpty()){
                        continue;
                    }
                    line = queue.peek();
                    if (line != null && line.equals(TERMINATING_ROW)) {
                        LOGGER.debug("found terminating row, shutting down");
                        break;
                    } else {
                        line = queue.poll();
                        numberOfLinesProcessed++;
                        calculator.calculate(line);
                    }*/


//                } finally {
//                    lock.unlock();
//                }
//            } else {
//                LOGGER.debug("unable to acquire lock");
//            }
//        }
        LOGGER.debug(numberOfLinesProcessed + " lines processed");
        return numberOfLinesProcessed;
    }
}
