package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class Calculator {
    private final CalculationStrategyProvider calculationStrategyProvider;
    private final MultiplierProvider multiplierProvider;
    private final InstrumentLineParser instrumentLineParser;

    public Calculator(CalculationStrategyProvider calculationStrategyProvider, MultiplierProvider multiplierProvider,
                      InstrumentLineParser instrumentLineParser){
        this.calculationStrategyProvider = calculationStrategyProvider;
        this.multiplierProvider = multiplierProvider;
        this.instrumentLineParser = instrumentLineParser;
    }

    public void calculate(String line){
        Instrument instrument = instrumentLineParser.parseLine(line);
        if (!isBusinessDay(instrument.getDate())){
            return;
        }
        calculationStrategyProvider.getCalculationStrategy(instrument.getInstrumentName())
                .calculateInstrumentMetric(instrument.getDate(),
                        adjustInstrumentPrice(instrument.getInstrumentName(), instrument.getPrice()));
    }

    private double adjustInstrumentPrice(String instrumentName, double instrumentPrice){
         return instrumentPrice * multiplierProvider.getMultiplierForInstrument(instrumentName);
    }

    private boolean isBusinessDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
}
