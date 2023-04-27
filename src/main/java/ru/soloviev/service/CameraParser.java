package ru.soloviev.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import ru.soloviev.entity.Camera;
import ru.soloviev.entity.CameraRequest;
import ru.soloviev.entity.UrlType;

import java.io.IOException;

public class CameraParser {
    ConnectionService connectionService = new ConnectionService();

    public Camera getCameraFromRequest(CameraRequest request) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        Camera camera = new Camera();
        camera.setId(request.getId());
            String jsonSourceData = connectionService.getJsonFromUrl(request.getSourceDataUrl());
            String jsonTokenData = connectionService.getJsonFromUrl(request.getTokenDataUrl());

            JsonParser jsonParser = jsonFactory.createParser(jsonSourceData);
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jsonParser.getCurrentName();
                String value = jsonParser.getValueAsString();
                if ((value != null) && (!value.equals(fieldName))) {
                    if (fieldName.equals("urlType")) {
                        camera.setUrlType(UrlType.valueOf(value));
                    }
                    if (fieldName.equals("videoUrl")) {
                        camera.setVideoUrl(value);
                    }
                }
            }
            jsonParser.close();

            jsonParser = jsonFactory.createParser(jsonTokenData);
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jsonParser.getCurrentName();
                String value = jsonParser.getValueAsString();
                if ((value != null) && (!value.equals(fieldName))) {
                    if (fieldName.equals("value")) {
                        camera.setValue(value);
                    }
                    if (fieldName.equals("ttl")) {
                        camera.setTtl(Integer.parseInt(value));
                    }
                }
            }
            jsonParser.close();
        return camera;
    }
}