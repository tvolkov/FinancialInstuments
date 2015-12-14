package com.infusion.reader.datasource;

import com.infusion.reader.NonParseableFileException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by tvolkov on 13.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class FileDataSourceTest {

    private FileDataSource fileDataSource;

    @Test
    public void shouldReturnNextLineOfFile(){

    }

    @Test(expected = NonParseableFileException.class)
    public void shouldThrowExceptionWhenFileHasNoCRLF(){

    }



}