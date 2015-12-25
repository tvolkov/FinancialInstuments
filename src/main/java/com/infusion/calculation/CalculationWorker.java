package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private final Calculator calculator;
    private final BlockingQueue<String> queue;
    private final LineParser lineParser = new InstrumentLineParser();
    private final Lock lock;
    private final Condition notEmpty;

    private long numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> queue, Calculator calculator, Lock lock, Condition notEmpty){
        this.queue = queue;
        this.calculator = calculator;
        this.lock = lock;
        this.notEmpty = notEmpty;
    }

    @Override
    public Long call() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String line;
        while (true){
            if(lock.tryLock()) {
                try {
                    /*
                     * we need to check if queue is empty since reader thread should wait for all the calculatorworkers before sending TERMINATING_ROW.
                     * Otherwise some threads might wait forever to take an element from queue
                     */
                    while (queue.isEmpty()) {
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            return numberOfLinesProcessed;
                        }
                        System.out.println(sdf.format(new Date()) + ": " + Thread.currentThread().getName() + ": queue is empty, waiting: "/* + countDownLatch.getCount()*/ );
                    }
                    line = queue.peek();
                    if (line != null && line.equals(TERMINATING_ROW)) {
                        System.out.println(sdf.format(new Date()) + ": " + Thread.currentThread().getName() + ": found terminating row, shutting down");
                        break;
                    } else {
                        line = queue.poll();
                        numberOfLinesProcessed++;
                        calculateValue(line);
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        System.out.println(Thread.currentThread().getName() + ": " + numberOfLinesProcessed + " lines processed");
        return numberOfLinesProcessed;
    }

    private void calculateValue(String line){
        calculator.calculate(lineParser.parseLine(line));
    }
}
