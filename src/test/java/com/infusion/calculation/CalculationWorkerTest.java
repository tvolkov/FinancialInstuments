package com.infusion.calculation;

import com.infusion.calculation.parser.LineParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculationWorkerTest {

    private static final String VALID_LINE = "VALID_LINE";
    private static final String TERMINATING_LINE = InstrumentMetricsCalculationEngine.TERMINATING_ROW;

    private CalculationWorker calculationWorker;

    @Mock
    private BlockingQueue<String> blockingQueue;

    @Mock
    private Calculator calculator;

    @Before
    public void setUp(){
        this.calculationWorker = new CalculationWorker(blockingQueue, calculator);
    }

    @Test
    public void shouldReturnTheNumberOfParsedLinesWhenFoundTerminatingRow(){
        //given
        when(blockingQueue.isEmpty()).thenReturn(false);
        when(blockingQueue.peek()).thenReturn(TERMINATING_LINE);

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        assertEquals(0, numberOfLinesProcessed);
    }


    @Test
    public void shouldPassTheLineToCalculatorAndIncreaseCounter() throws InterruptedException {
        //given
        when(blockingQueue.take()).thenReturn(VALID_LINE, TERMINATING_LINE);

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        verify(calculator).calculate(VALID_LINE);
        assertEquals(1, numberOfLinesProcessed);
    }

    @Test
    public void shouldReturnNumberOfLinesProcessedWhenInterruptedExceptionIsThrown() throws InterruptedException {
        //given
        when(blockingQueue.isEmpty()).thenReturn(true);
        doThrow(InterruptedException.class).when(blockingQueue).take();

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        assertEquals(0, numberOfLinesProcessed);
    }

}
