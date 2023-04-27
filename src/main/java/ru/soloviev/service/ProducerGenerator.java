package ru.soloviev.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AllArgsConstructor;
import ru.soloviev.entity.Camera;
import ru.soloviev.entity.CameraRequest;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class ProducerGenerator {
    private final String requestUrl;
    ConcurrentLinkedQueue<Camera> cameraBuffer;
    AtomicInteger counter;
    ConnectionService connectionService;

    public void start() throws IOException {
        counter.incrementAndGet();
        String jsonRequest = connectionService.getJsonFromUrl(requestUrl);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createParser(jsonRequest);

        CameraRequest cameraRequest = new CameraRequest();
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {

            if (jsonParser.currentToken() == JsonToken.END_OBJECT) {
                Thread producer = new Thread(new Producer(cameraRequest, cameraBuffer, counter));
                producer.start();
                cameraRequest = new CameraRequest();
            } else {
                String value = jsonParser.getValueAsString();
                if (value != null) {
                    String fieldName = jsonParser.getCurrentName();
                    if (!value.equals(fieldName)) {
                        if (fieldName.equals("id")) {
                            cameraRequest.setId(Integer.parseInt(value));
                        }
                        if (fieldName.equals("sourceDataUrl")) {
                            cameraRequest.setSourceDataUrl(value);
                        }
                        if (fieldName.equals("tokenDataUrl")) {
                            cameraRequest.setTokenDataUrl(value);
                        }
                    }
                }
            }
        }
        counter.decrementAndGet();
    }
}