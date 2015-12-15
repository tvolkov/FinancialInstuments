package com.infusion.reader;

import com.infusion.Row;
import com.infusion.reader.parser.LineParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tvolkov on 12.12.15.
 *
 * Although it violates SRP, and probably is more difficult to test,
 * I thought it would be redundant to write a class for
 * wrapping buffered reader / scanner / etc.
 * I decided to have this logic here, because since we need
 * to read file line by line (we can't just pass
 * the collection of lines), it would be too complicated
 * to have this logic in a separate class.
 */
public class SingleThreadedInputReader implements InputReader {

    private BlockingQueue<Row> blockingQueue;
    //TODO move lineParser to CalculationEngine
    private LineParser lineParser;
    private String pathToFile;

    public static final Row TERMINATING_ROW = new Row(null, null, Double.NaN);

    public SingleThreadedInputReader(String pathToFile, BlockingQueue<Row> blockingQueue, LineParser lineParser){
        this.pathToFile = pathToFile;
        this.blockingQueue = blockingQueue;
        this.lineParser = lineParser;
    }

    public void processInputData() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8)) {
            for (String line; (line = br.readLine()) != null;) {
                Row row = lineParser.parseLine(line);
                if (row != null){
                    blockingQueue.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            blockingQueue.add(TERMINATING_ROW);
        }

    }

    @Override
    public void run() {
        processInputData();
    }
}
