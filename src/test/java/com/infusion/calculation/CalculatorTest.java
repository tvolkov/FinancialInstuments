package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.MultiplierProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorTest {

    @Mock
    private CalculationStrategyProvider calculationStrategyProvider;

    @Mock
    private MultiplierProvider multiplierProvider;

    @Mock
    private InstrumentLineParser instrumentLineParser;

    @Mock
    private CalculationStrategy calculationStrategy;

    @Mock
    private DateValidator dateValidator;

    private Calculator calculator;

    private static final Instrument VALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2015, 12, 14), 1d);
    private static final Instrument INVALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2016, 1, 2), 1d);


    @Before
    public void setUp(){
        when(multiplierProvider.getMultiplierForInstrument(anyString())).thenReturn(1d);
        this.calculator = new Calculator(calculationStrategyProvider, multiplierProvider, instrumentLineParser, dateValidator);
    }

    @Test
    public void shouldCalculateMeanValueWhenThereIsAnEntryForTheGivenInstrument(){
        //given
        when(calculationStrategyProvider.getCalculationStrategy(VALID_INSTRUMENT.getInstrumentName())).thenReturn(calculationStrategy);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(VALID_INSTRUMENT);
        when(dateValidator.isDateValid(VALID_INSTRUMENT.getDate())).thenReturn(true);

        //when
        calculator.calculate(anyString());

        //then
        verify(calculationStrategyProvider, times(1)).getCalculationStrategy(VALID_INSTRUMENT.getInstrumentName());
        verify(calculationStrategy, times(1)).calculateInstrumentMetric(VALID_INSTRUMENT.getDate(), 1d);
    }

    @Test
    public void shouldSkipCalculationIfDateInRowIsNotValid(){
        //given
        when(instrumentLineParser.parseLine(anyString())).thenReturn(INVALID_INSTRUMENT);
        when(dateValidator.isDateValid(VALID_INSTRUMENT.getDate())).thenReturn(false);

        //when
        calculator.calculate(anyString());

        //then
        verifyZeroInteractions(calculationStrategyProvider);
    }
}
