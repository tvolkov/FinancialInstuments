package com.infusion.reader;

import com.infusion.Row;
import com.infusion.reader.parser.LineParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by tvolkov on 12.12.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class SingleThreadedInputReaderTest {

    private SingleThreadedInputReader singleThreadedInputReader;

    private static final Row TERMINATING_ROW = new Row(null, null, Double.NaN);

    private class EqualityMatcher<T> extends ArgumentMatcher<T> {

        private T object;

        public EqualityMatcher(T object){
            this.object = object;
        }

        @Override
        public boolean matches(Object argument) {
            return this.object.equals(argument);
        }
    }

    @Mock
    private BlockingQueue<Row> blockingQueue;

    @Mock
    private LineParser lineParser;

    @Before
    public void setup(){
        when(blockingQueue.add(any(Row.class))).thenReturn(true);
    }

    @Test
    public void shouldTerminateIfInputDataNotFoundOrEOFReached(){
        //given
        singleThreadedInputReader = new SingleThreadedInputReader("src/test/resources/non-existing-file.txt", blockingQueue, lineParser);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue, times(1)).add(argThat(new EqualityMatcher<>(TERMINATING_ROW)));
        verifyNoMoreInteractions(blockingQueue);
    }

    @Test
    public void shouldSkipTheLineIfItCantBeParsedAndPutNothingToQueue(){
        //given
        singleThreadedInputReader = new SingleThreadedInputReader("src/test/resources/incorrect_input_.txt", blockingQueue, lineParser);
        when(lineParser.parseLine(anyString())).thenReturn(null);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue, times(1)).add(argThat(new EqualityMatcher<>(TERMINATING_ROW)));
        verifyNoMoreInteractions(blockingQueue);
    }

    @Test
    public void shouldPutTheRowIntoQueueIfTheLineIsValid(){
        //given
        singleThreadedInputReader = new SingleThreadedInputReader("src/test/resources/example_input_short.txt", blockingQueue, lineParser);
        when(lineParser.parseLine(anyString())).thenReturn(new Row("", "", 0.0));

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue, times(2)).add(any(Row.class));
    }
}
