package ru.soloviev.service;

import lombok.AllArgsConstructor;
import ru.soloviev.entity.Camera;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class Consumer implements Runnable {
    private ConcurrentLinkedQueue<Camera> cameras;
    private ArrayList<Camera> cameraArrayList;
    private AtomicInteger counter;

    public void run() {
        while (counter.get() > 0) {
            while (!cameras.isEmpty()) {
                Camera camera = cameras.poll();
                cameraArrayList.add(camera);
            }
        }
    }
}