package com.infusion.reader;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;

public class SingleThreadedInputReader implements InputReader {

    private BlockingQueue<String> blockingQueue;
    private String pathToFile;
    private long linesRead;

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public SingleThreadedInputReader(String pathToFile, BlockingQueue<String> blockingQueue){
        this.pathToFile = pathToFile;
        this.blockingQueue = blockingQueue;
    }
    //todo if the large file doesn't have crlf's, then it will lead to oom exception
    public void processInputData() {
        /*try (BufferedReader br = Files.newBufferedReader(Paths.get(pathToFile), StandardCharsets.UTF_8)) {
            for (String line; (line = br.readLine()) != null;) {
                blockingQueue.add(line);
                linesRead++;
            }
        }*/
        try (FileInputStream fileInputStream = new FileInputStream(pathToFile);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            for (String line; (line = bufferedReader.readLine()) != null;){
                blockingQueue.add(line);
                linesRead++;
            }
        } catch (IOException e) {
            //todo throw more concrete exception here
            e.printStackTrace();
        } finally {
            System.out.println("Finished reading input file. Read " + linesRead + " lines");
            blockingQueue.add(TERMINATING_ROW);
        }
    }

    @Override
    public void run() {
        System.out.println("Starting reader thread");
        processInputData();
    }
}
