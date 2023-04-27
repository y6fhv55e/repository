package ru.soloviev.service;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.soloviev.entity.Camera;
import ru.soloviev.entity.CameraRequest;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class Producer implements Runnable {
    private final CameraRequest request;
    private ConcurrentLinkedQueue<Camera> cameras;
    private AtomicInteger counter;

    @SneakyThrows
    public void run() {
        CameraParser cameraParser = new CameraParser();
        counter.incrementAndGet();
        cameras.add(cameraParser.getCameraFromRequest(request));
        counter.decrementAndGet();
    }
}