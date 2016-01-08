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
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorTest {

    @Mock
    private Map<String, MeanCalculator> meanCalculatorMap;

    private MultiplierProvider multiplierProvider = new DummyMultiplierProvider();

    @Mock
    private InstrumentLineParser instrumentLineParser;

    @Mock
    private MeanCalculator meanCalculator;

    private Calculator calculator;

    private static final Instrument VALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2015, 12, 14), 1d);
    private static final Instrument INVALID_INSTRUMENT = new Instrument("rowName", LocalDate.of(2016, 1, 2), 1d);


    @Before
    public void setUp(){
        this.calculator = new Calculator(meanCalculatorMap, multiplierProvider, instrumentLineParser);
    }

    @Test
    public void shouldDoNothingIfMeanCalculatorMapDoesntHaveEntryForTheGivenInstrument(){
        //given
        when(meanCalculatorMap.containsKey(VALID_INSTRUMENT.getInstrumentName())).thenReturn(false);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(VALID_INSTRUMENT);

        //when
        calculator.calculate(anyString());

        //then
        verify(meanCalculatorMap, times(1)).containsKey(VALID_INSTRUMENT.getInstrumentName());
        verifyNoMoreInteractions(meanCalculatorMap);
    }

    @Test
    public void shouldCalculateMeanValueWhenThereIsAnEntryForTheGivenInstrument(){
        //given
        when(meanCalculatorMap.containsKey(VALID_INSTRUMENT.getInstrumentName())).thenReturn(true);
        when(meanCalculatorMap.get(VALID_INSTRUMENT.getInstrumentName())).thenReturn(meanCalculator);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(VALID_INSTRUMENT);

        //when
        calculator.calculate(anyString());

        //then
        verify(meanCalculatorMap, times(1)).containsKey(VALID_INSTRUMENT.getInstrumentName());
        verify(meanCalculatorMap, times(1)).get(VALID_INSTRUMENT.getInstrumentName());
        verify(meanCalculator, times(1)).increment(VALID_INSTRUMENT.getDate(), 1d);
    }

    @Test
    public void shouldSkipCalculationIfDateInRowIsNotABusinessDate(){
        //given
        when(meanCalculatorMap.containsKey(INVALID_INSTRUMENT.getInstrumentName())).thenReturn(false);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(INVALID_INSTRUMENT);

        //when
        calculator.calculate(anyString());

        //then
        verifyZeroInteractions(meanCalculatorMap);
    }
}
