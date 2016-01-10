package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calculator {
    private final CalculationStrategyProvider calculationStrategyProvider;
    private final MultiplierProvider multiplierProvider;
    private final InstrumentLineParser instrumentLineParser;
    private final DateValidator dateValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(Calculator.class);

    public Calculator(CalculationStrategyProvider calculationStrategyProvider, MultiplierProvider multiplierProvider,
                      InstrumentLineParser instrumentLineParser, DateValidator dateValidator){
        this.calculationStrategyProvider = calculationStrategyProvider;
        this.multiplierProvider = multiplierProvider;
        this.instrumentLineParser = instrumentLineParser;
        this.dateValidator = dateValidator;
    }

    public void calculate(String line){
        Instrument instrument = instrumentLineParser.parseLine(line);
        if (!dateValidator.isDateValid(instrument.getDate())){
            return;
        }
        calculationStrategyProvider.getCalculationStrategy(instrument.getInstrumentName())
                .calculateInstrumentMetric(instrument.getDate(),
                        adjustInstrumentPrice(instrument.getInstrumentName(), instrument.getPrice()));
    }

    private double adjustInstrumentPrice(String instrumentName, double instrumentPrice){
         return instrumentPrice * multiplierProvider.getMultiplierForInstrument(instrumentName);
    }
}
