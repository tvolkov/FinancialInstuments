package com.infusion.reader.datasource;

import java.io.IOException;

/**
 * Created by tvolkov on 13.12.2015.
 */
public interface InputDataSource {
    boolean hasNextLine();
    String getNextLine() throws IOException;
}
