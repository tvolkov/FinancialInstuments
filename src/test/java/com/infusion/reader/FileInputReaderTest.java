package com.infusion.reader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileInputReaderTest {

    private FileInputReader singleThreadedInputReader;

    private static final File NON_EXISTIING_FILE = new File("src/test/resources/non-existing-file.txt");
    private static final File EXISTIING_FILE = new File("src/test/resources/example_input_short.txt");

    @Mock
    private BlockingQueue<String> blockingQueue;

    @Mock
    private CountDownLatch countDownLatch;

    @Before
    public void setup(){
        when(blockingQueue.add(any(String.class))).thenReturn(true);
    }

    @Test
    public void shouldTerminateIfInputDataNotFoundOrEOFReached() throws InterruptedException {
        //given
        singleThreadedInputReader = new FileInputReader(NON_EXISTIING_FILE, blockingQueue, countDownLatch);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verifyZeroInteractions(blockingQueue);
    }

    @Test
    public void shouldPutTheRowIntoQueueIfTheLineIsValid() throws InterruptedException {
        //given
        singleThreadedInputReader = new FileInputReader(EXISTIING_FILE, blockingQueue, countDownLatch);

        //when
        singleThreadedInputReader.processInputData();

        //then
        verify(blockingQueue, times(1)).put(any(String.class));
    }
}
