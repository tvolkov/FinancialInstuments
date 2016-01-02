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

    private static final Row ROW = new Row("rowName", LocalDate.now(), 1d);

    @Before
    public void setUp(){
        this.calculator = new Calculator(meanCalculatorMap, multiplierProvider, instrumentLineParser);
    }

    @Test
    public void shouldDoNothingIfMeanCalculatorMapDoesntHaveEntryForTheGivenInstrument(){
        //given
        when(meanCalculatorMap.containsKey(ROW.getInstrumentName())).thenReturn(false);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(ROW);

        //when
        calculator.calculate(anyString());

        //then
        verify(meanCalculatorMap, times(1)).containsKey(ROW.getInstrumentName());
        verifyNoMoreInteractions(meanCalculatorMap);
    }

    @Test
    public void shouldCalculateMeanValueWhenThereIsAnEntryForTheGivenInstrument(){
        //given
        when(meanCalculatorMap.containsKey(ROW.getInstrumentName())).thenReturn(true);
        when(meanCalculatorMap.get(ROW.getInstrumentName())).thenReturn(meanCalculator);
        when(instrumentLineParser.parseLine(anyString())).thenReturn(ROW);

        //when
        calculator.calculate(anyString());

        //then
        verify(meanCalculatorMap, times(1)).containsKey(ROW.getInstrumentName());
        verify(meanCalculatorMap, times(1)).get(ROW.getInstrumentName());
        verify(meanCalculator, times(1)).increment(ROW.getDate(), 1d);
    }
}
