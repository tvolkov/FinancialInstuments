package com.infusion.calculation;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MeanCalculatorTest {

    @Mock
    private Mean mean;

    private MeanCalculator meanCalculator;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy").withLocale(Locale.US);

    @Before
    public void setUp() throws ParseException {
        LocalDate averagingPeriodStart = LocalDate.parse("01-Nov-2014", FORMATTER);
        LocalDate averagingPeriodEnd = LocalDate.parse("30-Nov-2014", FORMATTER);
        meanCalculator = new MeanCalculator(mean, averagingPeriodStart, averagingPeriodEnd);
    }

    @Test
    public void shouldIncrementIfGivenDateIsWithingAveragingPeriod() throws ParseException {
        //given
        LocalDate actualDate = LocalDate.parse("22-Nov-2014", FORMATTER);

        //when
        meanCalculator.increment(actualDate, 1d);

        //then
        verify(mean, times(1)).increment(1d);
        verifyNoMoreInteractions(mean);
    }

    @Test
    public void shouldNotIncrementIfGivenDateIsNotWithingAveragingPeriod() throws ParseException {
        //given
        LocalDate actualDate = LocalDate.parse("28-Dec-2015", FORMATTER);

        //when
        meanCalculator.increment(actualDate, 1d);

        //then
        verify(mean, never()).increment(1d);
    }
}
