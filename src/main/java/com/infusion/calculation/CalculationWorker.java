package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.calculation.parser.LineParser;
import com.infusion.correction.CorrectionProvider;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import static com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

/**
 * Created by tvolkov on 16.12.2015.
 */
public class CalculationWorker implements Callable {

    private BlockingQueue<String> blockingQueue;
    private Map<String, MeanCalculator> meanCalculatorMap;
    //todo probably make it a singletone
    private LineParser lineParser;
    private CorrectionProvider correctionProvider;
    private int numberOfLinesProcessed;

    public CalculationWorker(BlockingQueue<String> blockingQueue, Map<String, MeanCalculator> meanCalculatorMap,
                             CorrectionProvider correctionProvider){
        this.blockingQueue = blockingQueue;
        this.meanCalculatorMap = meanCalculatorMap;
        this.correctionProvider = correctionProvider;
        this.lineParser = new InstrumentLineParser();
    }

    @Override
    public Object call() {
        try {
            String line;
            while (!((line = blockingQueue.take()).equals(TERMINATING_ROW))){
                numberOfLinesProcessed++;
                Row row = lineParser.parseLine(line);
                if (meanCalculatorMap.containsKey(row.getIntrumentName())){
                    meanCalculatorMap.get(row.getIntrumentName()).increment(row.getDate(),
                        row.getPrice() * correctionProvider.getCorrectionForInstrument(row.getIntrumentName()));
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            return numberOfLinesProcessed;
        }
    }
}
