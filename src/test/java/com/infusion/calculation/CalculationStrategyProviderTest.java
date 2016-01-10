package com.infusion.calculation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculationStrategyProviderTest {

    private CalculationStrategyProvider calculationStrategyProvider;

    @Mock
    private Map<String, CalculationStrategy> calculationStrategyMap;

    @Mock
    private CalculationStrategy calculationStrategy;

    private static final String KEY = "INSTRUMENT1";
    private static final int NUMBER_OF_NEWEST = 10;

    @Test
    public void shouldPutNewDefaultCalculationStrategyIfAbsentAndReturnIt() {
        //given
        when(calculationStrategyMap.containsKey(KEY)).thenReturn(false);
        when(calculationStrategyMap.get(KEY)).thenReturn(calculationStrategy);
        calculationStrategyProvider = new CalculationStrategyProvider(calculationStrategyMap, NUMBER_OF_NEWEST);

        //when
        CalculationStrategy newCalculationStrategy = calculationStrategyProvider.getCalculationStrategy(KEY);

        //then
        assertEquals(calculationStrategy, newCalculationStrategy);
        verify(calculationStrategyMap).put(eq(KEY), any(CalculationStrategy.class));
    }

    @Test
    public void shouldReturnACalculationStrategyByGivenKey() {
        //given
        when(calculationStrategyMap.containsKey(KEY)).thenReturn(true);
        when(calculationStrategyMap.get(KEY)).thenReturn(calculationStrategy);
        calculationStrategyProvider = new CalculationStrategyProvider(calculationStrategyMap, NUMBER_OF_NEWEST);

        //when
        CalculationStrategy newCalculationStrategy = calculationStrategyProvider.getCalculationStrategy(KEY);

        //then
        assertEquals(calculationStrategy, newCalculationStrategy);
        verify(calculationStrategyMap, never()).put(eq(KEY), any(CalculationStrategy.class));
    }
}