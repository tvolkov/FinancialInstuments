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
public class InstrumentMetricCalculationEngine implements CalculationEngine {

    private InputReader inputReader;
    private BlockingQueue<Row> blockingQueue;
    private CorrectionProvider correctionProvider;
    private Map<String, MeanCalculator> meanCalculatorMap;

    public InstrumentMetricCalculationEngine(String pathToFile, Map<String, MeanCalculator> meanCalculatorMap, CorrectionProvider correctionProvider){
        this.blockingQueue = new ArrayBlockingQueue<>(1000);
        this.inputReader = new SingleThreadedInputReader(pathToFile, blockingQueue, new InstrumentLineParser());
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
    }

    @Override
    public void calculateMetrics() {
        (new Thread(inputReader)).start();

        try {
            Row row;
            while (!((row = blockingQueue.take()).equals(TERMINATING_ROW))){
                if (meanCalculatorMap.containsKey(row.getIntrumentName())){
                    meanCalculatorMap.get(row.getIntrumentName()).increment(row.getDate(),
                            row.getPrice() * correctionProvider.getCorrectionForInstrument(row.getIntrumentName()));
                }
            }
        } catch (InterruptedException e) {
            return;
        } finally {
            printCalculatedMetrics();
        }

    }

    private void printCalculatedMetrics() {
        for (Map.Entry<String, MeanCalculator> entry : meanCalculatorMap.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue().getResult());
        }
    }
}
