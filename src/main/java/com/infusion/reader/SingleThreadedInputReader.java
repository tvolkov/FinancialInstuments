package com.infusion.reader;

import com.infusion.Row;
import com.infusion.reader.parser.LineParser;
import com.infusion.reader.datasource.InputDataSource;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RunnableFuture;

/**
 * Created by tvolkov on 12.12.15.
 */
public class SingleThreadedInputReader implements InputReader {

    private BlockingQueue<Row> blockingQueue;
    private InputDataSource dataSource;
    private LineParser lineParser;

    private static final Row TERMINATING_ROW = new Row(null, null, null);

    public SingleThreadedInputReader(BlockingQueue<Row> blockingQueue, InputDataSource inputDataSource, LineParser lineParser){
        this.blockingQueue = blockingQueue;
        this.dataSource = inputDataSource;
        this.lineParser = lineParser;
    }

    public void processInputData() {
        while (dataSource.hasNextLine()){
            Row row = lineParser.parseLine(dataSource.getNextLine());
            if (row != null){
                blockingQueue.add(row);
            }
        }
        blockingQueue.add(TERMINATING_ROW);
    }
}
