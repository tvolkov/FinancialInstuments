package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

public class Calculator {
    private final Map<String, MeanCalculator> meanCalculatorMap;
    private final MultiplierProvider multiplierProvider;
    private final InstrumentLineParser instrumentLineParser;

    public Calculator(Map<String, MeanCalculator> meanCalculatorMap, MultiplierProvider multiplierProvider, InstrumentLineParser instrumentLineParser){
        this.meanCalculatorMap = meanCalculatorMap;
        this.multiplierProvider = multiplierProvider;
        this.instrumentLineParser = instrumentLineParser;
    }

    public void calculate(String line){
        Instrument instrument = instrumentLineParser.parseLine(line);
        if (!isBusinessDay(instrument.getDate())){
            return;
        }
        if (meanCalculatorMap.containsKey(instrument.getInstrumentName())) {
            meanCalculatorMap.get(instrument.getInstrumentName()).increment(instrument.getDate(),
                    instrument.getPrice() * multiplierProvider.getMultiplierForInstrument(instrument.getInstrumentName()));
        }
    }

    private boolean isBusinessDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
