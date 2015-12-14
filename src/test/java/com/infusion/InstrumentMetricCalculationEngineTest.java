package com.infusion;

import com.infusion.reader.InputReader;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.BlockingQueue;

/**
 * Created by tvolkov on 12/14/15.
 */
public class InstrumentMetricCalculationEngineTest {

    private InstrumentMetricCalculationEngine instrumentMetricCalculationEngine;

    @Mock
    private InputReader inputReader;

    @Mock
    private BlockingQueue<Row> blockingQueue;

}