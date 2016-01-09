package com.infusion.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class FileInputReader implements InputReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileInputReader.class);
    private final BlockingQueue<String> queue;
    private final File inputFile;
    private final CountDownLatch countDownLatch;

    private long linesRead;

    public FileInputReader(File inputFile, BlockingQueue<String> queue, CountDownLatch countDownLatch) {
        this.inputFile = inputFile;
        this.queue = queue;
        this.countDownLatch = countDownLatch;
    }

    //todo if the large file doesn't have crlf's, then it will lead to oom exception
    public void processInputData() {
        if (!inputFile.exists()) {
            LOGGER.debug("file does not exist, aborting");
            return;
        }
        LOGGER.debug("start reading file");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))){
            for (String line; (line = bufferedReader.readLine()) != null;){
                queue.put(line);
                linesRead++;
            }
            //todo catch these exception an appropriate way
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        countDownLatch.countDown();
        LOGGER.debug("finished reading file, lines read " + linesRead);
    }

    @Override
    public void run() {
        processInputData();
    }
}
