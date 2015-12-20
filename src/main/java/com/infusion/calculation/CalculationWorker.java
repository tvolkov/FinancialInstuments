package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;
import com.infusion.correction.CorrectionProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private final BlockingQueue<String> queue;
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final LineParser lineParser = new InstrumentLineParser();
    private final CorrectionProvider correctionProvider;
    private long numberOfLinesProcessed;
    private final Lock lock;
    private final CountDownLatch countDownLatch;

    public CalculationWorker(BlockingQueue<String> queue, Map<String, MeanCalculator> meanCalculatorMap,
                             CorrectionProvider correctionProvider, Lock lock, CountDownLatch countDownLatch){
        this.queue = queue;
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
        this.lock = lock;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public Long call() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String line;
        while (true){
            if(lock.tryLock()) {
                try {
                    while (queue.isEmpty()) {
                        System.out.println(sdf.format(new Date()) + ": " + Thread.currentThread().getName() + ": queue is empty, waiting");
                        countDownLatch.countDown();
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
        Row row = lineParser.parseLine(line);
        if (row != null){
            if (meanCalculatorMap.containsKey(row.getIntrumentName())){
                meanCalculatorMap.get(row.getIntrumentName()).increment(row.getDate(),
                        row.getPrice() * correctionProvider.getCorrectionForInstrument(row.getIntrumentName()));
            }
        } else {
            throw new RuntimeException("Row is null");
        }
    }
}
