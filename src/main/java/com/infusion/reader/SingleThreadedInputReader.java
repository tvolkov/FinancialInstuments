package com.infusion.reader;

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

    private BlockingQueue<String> blockingQueue;
    //TODO move lineParser to CalculationEngine
    private String pathToFile;

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public SingleThreadedInputReader(String pathToFile, BlockingQueue<String> blockingQueue){
        this.pathToFile = pathToFile;
        this.blockingQueue = blockingQueue;
    }
    //todo if the large file doesn't have crlf's, then it will lead to oom exception
    public void processInputData() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8)) {
            for (String line; (line = br.readLine()) != null;) {
                blockingQueue.add(line);
            }
        } catch (IOException e) {
            //todo throw more concrete exception here
            e.printStackTrace();
        } finally {
            blockingQueue.add(TERMINATING_ROW);
        }
    }

    @Override
    public void run() {
        processInputData();
    }
}
