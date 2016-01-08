package com.infusion.calculation;

import com.infusion.calculation.parser.InstrumentLineParser;
import com.infusion.correction.DummyMultiplierProvider;
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

    private MultiplierProvider multiplierProvider = new DummyMultiplierProvider();

    @Mock
    private InstrumentLineParser instrumentLineParser;

    @Mock
    private CalculationStrategy calculationStrategy;

    private Calculator calculator;

    private static final Instrument VALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2015, 12, 14), 1d);
    private static final Instrument INVALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2016, 1, 2), 1d);


    @Before
    public void setUp(){
        this.calculator = new Calculator(calculationStrategyProvider, multiplierProvider, instrumentLineParser);
    }

    @Test
    public void shouldCalculateMeanValueWhenThereIsAnEntryForTheGivenInstrument(){
        //given
        when(calculationStrategyProvider.getCalculationStrategy(VALID_INSTRUMENT.getInstrumentName())).thenReturn(calculationStrategy);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(VALID_INSTRUMENT);

        //when
        calculator.calculate(anyString());

        //then
        verify(calculationStrategyProvider, times(1)).getCalculationStrategy(VALID_INSTRUMENT.getInstrumentName());
        verify(calculationStrategy, times(1)).calculateInstrumentMetric(VALID_INSTRUMENT.getDate(), 1d);
    }

    @Test
    public void shouldSkipCalculationIfDateInRowIsNotABusinessDate(){
        //given
        when(instrumentLineParser.parseLine(anyString())).thenReturn(INVALID_INSTRUMENT);

        //when
        calculator.calculate(anyString());

        //then
        verifyZeroInteractions(calculationStrategyProvider);
    }
}
