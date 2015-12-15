package com.infusion;

import com.infusion.correction.CorrectionProvider;
import com.infusion.reader.InputReader;
import com.infusion.reader.SingleThreadedInputReader;
import com.infusion.reader.parser.InstrumentLineParser;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

/**
 * Created by tvolkov on 12/14/15.
 */
public class InstrumentMeanValuesCalculationEngine implements CalculationEngine {

    private InputReader inputReader;
    private BlockingQueue<Row> blockingQueue;
    private CorrectionProvider correctionProvider;
    private Map<String, MeanCalculator> meanCalculatorMap;

    private long totalExecutionTime;
    private long numberOfLinesProcessed;

    private static final int DEFAULT_QUEUE_CAPACITY = 100000;

    public InstrumentMeanValuesCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap, CorrectionProvider correctionProvider){
        this.blockingQueue = new ArrayBlockingQueue<>(DEFAULT_QUEUE_CAPACITY);
        this.inputReader = new SingleThreadedInputReader(pathToFile, blockingQueue, new InstrumentLineParser());
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
    }

    @Override
    public void calculateMetrics() {   
        long startTime = System.currentTimeMillis();
        (new Thread(inputReader)).start();

        try {
            Row row;
            while (!((row = blockingQueue.take()).equals(TERMINATING_ROW))){
                numberOfLinesProcessed++;
                if (meanCalculatorMap.containsKey(row.getIntrumentName())){
                    meanCalculatorMap.get(row.getIntrumentName()).increment(row.getDate(),
                            row.getPrice() * correctionProvider.getCorrectionForInstrument(row.getIntrumentName()));
                }
            }
        } catch (InterruptedException e) {
            return;
        } finally {
            long endTime = System.currentTimeMillis();
            this.totalExecutionTime = (endTime - startTime ) / 1000;
            printInfo();
        }

    }
    //TODO create interface like MetricsPrinter to allow output to different places
    private void printInfo() {
        System.out.println("Calculated values:");
        for (Map.Entry<String, MeanCalculator> entry : meanCalculatorMap.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());
        }
        System.out.println("------------------");
        System.out.println("Number of lines processed: " + numberOfLinesProcessed);
        System.out.println("------------------");
        System.out.println("Time elapsed: " + totalExecutionTime);
    }
}
