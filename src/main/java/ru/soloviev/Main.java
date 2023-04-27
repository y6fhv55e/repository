package ru.soloviev;

import ru.soloviev.entity.Camera;
import ru.soloviev.service.ConnectionService;
import ru.soloviev.service.Consumer;
import ru.soloviev.service.ProducerGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        String requestUrl = "http://www.mocky.io/v2/5c51b9dd3400003252129fb5";
        
        ConcurrentLinkedQueue<Camera> cameraBuffer = new ConcurrentLinkedQueue<Camera>();
        ArrayList<Camera> resultCameraArrayList = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        ProducerGenerator producerGenerator = new ProducerGenerator(
                requestUrl,
                cameraBuffer,
                counter,
                new ConnectionService());
        try {
            producerGenerator.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread consumer = new Thread(new Consumer(cameraBuffer, resultCameraArrayList, counter));
        consumer.start();
        try {
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(resultCameraArrayList);
    }
}