package com.infusion.reader;

import com.infusion.Row;
import com.infusion.reader.datasource.InputDataSource;
import com.infusion.reader.parser.LineParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.assertEquals;
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

    private Row terminatingRow = new Row(null, null, null);

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
    private InputDataSource inputDataSource;

    @Mock
    private LineParser lineParser;

    @Before
    public void setup(){
        when(blockingQueue.add(any(Row.class))).thenReturn(true);
        this.singleThreadedInputReader = new SingleThreadedInputReader(blockingQueue, inputDataSource, lineParser);
    }

    @Test
    public void shouldTerminateIfInputDataNotFoundOrEOFReached(){
        //given
        when(inputDataSource.hasNextLine()).thenReturn(false);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue).add(argThat(new EqualityMatcher<Row>(terminatingRow)));
        assertEquals(0, blockingQueue.size());
    }

    @Test
    public void shouldSkipTheLineIfItCantBeParsedAndPutNothingToQueue(){
        //given
        when(inputDataSource.hasNextLine()).thenReturn(true).thenReturn(false);
        when(inputDataSource.getNextLine()).thenReturn("");
        when(lineParser.parseLine(anyString())).thenReturn(null);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(inputDataSource, times(1)).getNextLine();
    }

    @Test
    public void shouldPutTheRowIntoQueueIfTheLineIsValid(){
        //given
        when(inputDataSource.hasNextLine()).thenReturn(true).thenReturn(false);
        when(lineParser.parseLine(anyString())).thenReturn(new Row("", "", ""));

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue, times(2)).add(any(Row.class));
    }
}
