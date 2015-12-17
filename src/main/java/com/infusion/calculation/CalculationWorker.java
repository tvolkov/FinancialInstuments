package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;
import com.infusion.correction.CorrectionProvider;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

public class CalculationWorker implements Callable<Long> {

    private BlockingQueue<String> blockingQueue;
    //todo this map needs to be synchronized
    private Map<String, MeanCalculator> meanCalculatorMap;
    //todo probably make it a singletone
    private LineParser lineParser = new InstrumentLineParser();
    private CorrectionProvider correctionProvider;
    private long numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> blockingQueue, Map<String, MeanCalculator> meanCalculatorMap,
                             CorrectionProvider correctionProvider){
        this.blockingQueue = blockingQueue;
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
    }

    @Override
    public Long call() {
        try {
            String line;
            while (true){
                line = blockingQueue.peek();
                if (line != null && line.equals(TERMINATING_ROW)){
                    System.out.println(Thread.currentThread().getName() + ": found terminating row, shutting down");
                    break;
                } else {
                    if (blockingQueue.isEmpty()){
                        System.out.println(Thread.currentThread().getName() + ": Queue is empty, waiting");
                    }
                    line = blockingQueue.take();
                    numberOfLinesProcessed++;
                    calculateValue(line);
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
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
        }
    }
}
