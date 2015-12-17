package com.infusion.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

/**
 * Created by tvolkov on 12.12.15.
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
