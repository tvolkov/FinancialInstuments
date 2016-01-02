package com.infusion.calculation;

import com.infusion.calculation.CalculationWorker;
import com.infusion.calculation.Calculator;
import com.infusion.calculation.parser.LineParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CalculationWorkerTest {

    private static final String VALID_LINE = "VALID_LINE";
    private static final String TERMINATING_LINE = com.infusion.reader.SingleThreadedInputReader.TERMINATING_ROW;

    private CalculationWorker calculationWorker;

    @Mock
    private BlockingQueue<String> blockingQueue;

    @Mock
    private LineParser lineParser;

    @Mock
    private Calculator calculator;

    @Mock
    private Condition notEmpty;

    @Mock
    private Lock lock;

    @Before
    public void setUp(){
        this.calculationWorker = new CalculationWorker(blockingQueue, calculator, lock, notEmpty);
    }

    @Test
    public void shouldReturnTheNumberOfParsedLinesWhenFoundTerminatingRow(){
        //given
        when(lock.tryLock()).thenReturn(true);
        when(blockingQueue.isEmpty()).thenReturn(false);
        when(blockingQueue.peek()).thenReturn(TERMINATING_LINE);

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        assertEquals(0, numberOfLinesProcessed);
    }

    @Test
    public void shouldWaitWhileQueueIsEmpty() throws InterruptedException {
        //given
        when(lock.tryLock()).thenReturn(true);
        when(blockingQueue.isEmpty()).thenReturn(true, false);
        when(blockingQueue.peek()).thenReturn(TERMINATING_LINE);

        //when
        calculationWorker.call();

        //then
        verify(notEmpty).await();
    }

    @Test
    public void shouldPassTheLineToCalculatorAndIncreaseCounter(){
        //given
        when(lock.tryLock()).thenReturn(true);
        when(blockingQueue.isEmpty()).thenReturn(false);
        when(blockingQueue.peek()).thenReturn(VALID_LINE, TERMINATING_LINE);
        when(blockingQueue.poll()).thenReturn(VALID_LINE);

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        verify(calculator).calculate(VALID_LINE);
        assertEquals(1, numberOfLinesProcessed);
    }

    @Test
    public void shouldReturnNumberOfLinesProcessedWhenInterruptedExceptionIsThrown() throws InterruptedException {
        //given
        when(lock.tryLock()).thenReturn(true);
        when(blockingQueue.isEmpty()).thenReturn(true);
        doThrow(InterruptedException.class).when(notEmpty).await();

        //when
        long numberOfLinesProcessed = calculationWorker.call();

        //then
        assertEquals(0, numberOfLinesProcessed);
    }

}
