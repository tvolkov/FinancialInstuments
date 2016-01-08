package com.infusion.calculation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCalculationStrategyTest {

    private DefaultCalculationStrategy defaultCalculationStrategy;

    private static final int NUMBER_OF_ELEMENTS_TO_SUM = 10;

    @Test
    public void shouldRemoveGreatestValueFromMapDoesNotHaveKeyGreaterThanGiven(){
        //given
        defaultCalculationStrategy = new DefaultCalculationStrategy(NUMBER_OF_ELEMENTS_TO_SUM);

        //when
        for (int i = 1980; i <= 1989; i++){
            defaultCalculationStrategy.calculateInstrumentMetric(LocalDate.of(i, 1, 1), 1d);
        }
        for (int i = 1990; i <= 1999; i++){
            defaultCalculationStrategy.calculateInstrumentMetric(LocalDate.of(i, 1, 1), 2d);
        }

        //then
        assertEquals(20d, defaultCalculationStrategy.getResult(), 0d);
    }

    @Test
    public void shouldRemoveValueFromMapIfMapContainsValueLessThanGiven(){

    }

    @Test
    public void shouldPutValueToTheMapIfItsSizeIsLessThanTen(){

    }

    @Test
    public void shouldReturnSumOfTheValuesInTheMap(){

    }
}