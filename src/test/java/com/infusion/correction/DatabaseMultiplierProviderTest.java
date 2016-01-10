package com.infusion.correction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseMultiplierProviderTest {
    private DatabaseMultiplierProvider databaseMultiplierProvider;

    @Mock
    private DatabaseQueryRunner databaseQueryRunner;

    private static final String INSTRUMENT_NAME = "INSTRUMENT1";

    @Test
    public void shouldUpdateValueInCacheAndReturnIt() throws InterruptedException {
        //given
        when(databaseQueryRunner.getMultiplierForInstument(INSTRUMENT_NAME)).thenReturn(1d).thenReturn(2d);
        databaseMultiplierProvider = new DatabaseMultiplierProvider(databaseQueryRunner);

        //when
        double price1 = databaseMultiplierProvider.getMultiplierForInstrument(INSTRUMENT_NAME);

        //then
        assertEquals(1d, price1, 0d);
        verify(databaseQueryRunner, times(1)).getMultiplierForInstument(INSTRUMENT_NAME);
    }
}