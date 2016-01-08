package com.infusion;

import com.infusion.calculation.InstrumentMeanValuesCalculationEngine;
import com.infusion.calculation.Instrument;
import com.infusion.reader.InputReader;
import org.mockito.Mock;

import java.util.concurrent.BlockingQueue;

/**
 * Created by tvolkov on 12/14/15.
 */
public class InstrumentMetricCalculationEngineTest {

    private InstrumentMeanValuesCalculationEngine instrumentMeanValuesCalculationEngine;

    @Mock
    private InputReader inputReader;

    @Mock
    private BlockingQueue<Instrument> blockingQueue;

}