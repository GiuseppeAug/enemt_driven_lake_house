package com.example;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        CreateEdgeTable.createEdgeTable();

        // Create and start the threads
        Thread dataSenderThread = new Thread(() -> DataSender.sendData());
        Thread streamModelingThread = new Thread(() -> StreamingModelingAndStorage.streamModelingAndStorage());

        dataSenderThread.start();
        streamModelingThread.start();
    }
}
