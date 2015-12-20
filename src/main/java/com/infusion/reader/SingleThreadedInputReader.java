package com.infusion.reader;

import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class SingleThreadedInputReader implements InputReader {

    private final BlockingQueue<String> queue;
    private final String pathToFile;
    private long linesRead;
    private final Lock lock;
    private final CountDownLatch countDownLatch;

    public static final String TERMINATING_ROW = "####END_OF_DATA####";

    public SingleThreadedInputReader(String pathToFile, BlockingQueue<String> queue, Lock lock, CountDownLatch countDownLatch){
        this.pathToFile = pathToFile;
        this.queue = queue;
        this.lock = lock;
          this.countDownLatch = countDownLatch;
    }
    //todo if the large file doesn't have crlf's, then it will lead to oom exception
    public void processInputData() {
        if (Files.notExists(Paths.get(pathToFile))){
            System.out.println(Thread.currentThread().getName() + ": file does not exist, adding termination");
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
            System.out.println(Thread.currentThread().getName() + ": Finished reading input file. Read " + linesRead + " lines. adding terminator");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            queue.add(TERMINATING_ROW);
            System.out.println(Thread.currentThread().getName() + ": EOF reached, exiting");
        }
    }

    @Override
    public void run() {
        System.out.println("Starting reader thread");
        processInputData();
        System.out.println(Thread.currentThread().getName() + ": exiting reader thread");
    }
}
