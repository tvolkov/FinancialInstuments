package com.infusion.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SingleThreadedInputReader implements InputReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleThreadedInputReader.class);

    private final BlockingQueue<String> queue;
    private final String pathToFile;
    private long linesRead;
    private final Lock lock;
    private final Condition notEmpty;

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public SingleThreadedInputReader(String pathToFile, BlockingQueue<String> queue, Lock lock, Condition notEmpty){
        this.pathToFile = pathToFile;
        this.queue = queue;
        this.lock = lock;
        this.notEmpty = notEmpty;
    }
    //todo if the large file doesn't have crlf's, then it will lead to oom exception
    public void processInputData() {
        if (Files.notExists(Paths.get(pathToFile))){
            LOGGER.debug("file does not exist, adding termination");
            queue.add(TERMINATING_ROW);
            return;
        }
        try (FileInputStream fileInputStream = new FileInputStream(pathToFile);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
            for (String line; (line = bufferedReader.readLine()) != null;){
                queue.add(line);
                linesRead++;
            }
        } catch (IOException e) {
            //todo throw more concrete exception here
            e.printStackTrace();
        } finally {
            LOGGER.debug("Finished reading input file. Read " + linesRead + " lines. adding terminator");
            lock.lock();
            try {
                queue.add(TERMINATING_ROW);
                notEmpty.signalAll();
            } finally {
                lock.unlock();
            }
            LOGGER.debug("EOF reached, exiting");
        }
    }

    @Override
    public void run() {
        processInputData();
        LOGGER.debug("exiting reader thread");
    }
}
