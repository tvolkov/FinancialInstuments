//package com.infusion.reader;
//
//import com.infusion.calculation.Instrument;
//import com.infusion.calculation.parser.LineParser;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentMatcher;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Matchers.argThat;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verifyNoMoreInteractions;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.times;
//
//import static com.infusion.reader.FileInputReader.TERMINATING_ROW;
//
//@RunWith(MockitoJUnitRunner.class)
//public class SingleThreadedInputReaderTest {
//
//    private FileInputReader singleThreadedInputReader;
//
//    private class EqualityMatcher<T> extends ArgumentMatcher<T> {
//
//        private T object;
//
//        public EqualityMatcher(T object){
//            this.object = object;
//        }
//
//        @Override
//        public boolean matches(Object argument) {
//            return this.object.equals(argument);
//        }
//    }
//
//    @Mock
////    private ConcurrentLinkedQueue<String> blockingQueue;
//    private BlockingQueue<String> blockingQueue;
//
//    @Mock
//    private Lock lock;
//
//    @Mock
//    private Condition notEmpty;
//
//    @Before
//    public void setup(){
//        when(blockingQueue.add(any(String.class))).thenReturn(true);
//    }
//
//    @Test
//    public void shouldTerminateIfInputDataNotFoundOrEOFReached(){
//        //given
//        singleThreadedInputReader = new FileInputReader("src/test/resources/non-existing-file.txt", blockingQueue, lock, notEmpty);
//
//        //when
//        singleThreadedInputReader.processInputData();
//
//        //then
//        verify(blockingQueue, times(1)).add(argThat(new EqualityMatcher<>(TERMINATING_ROW)));
//        verifyNoMoreInteractions(blockingQueue);
//    }
//
//    @Test
//    public void shouldPutTheRowIntoQueueIfTheLineIsValid(){
//        //given
//        singleThreadedInputReader = new FileInputReader("src/test/resources/example_input_short.txt", blockingQueue, lock, notEmpty);
//
//        //when
//        singleThreadedInputReader.processInputData();
//
//        //then
//        verify(blockingQueue, times(2)).add(any(String.class));
//    }
//}
