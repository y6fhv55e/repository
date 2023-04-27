package ru.soloviev;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.soloviev.entity.Camera;
import ru.soloviev.entity.CameraRequest;
import ru.soloviev.entity.UrlType;
import ru.soloviev.service.CameraParser;
import ru.soloviev.service.ConnectionService;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CameraParserTest {

    @InjectMocks
    CameraParser subj;

    @Mock
    ConnectionService connectionService;

    @Test
    public void getCameraFromRequest_success() throws IOException {
        String sourceDataUrl = "sourceDataUrl";
        String tokenDataUrl = "tokenDataUrl";
        Camera expectedCamera = new Camera();
        expectedCamera.setId(1);
        expectedCamera.setUrlType(UrlType.LIVE);
        expectedCamera.setVideoUrl("test_video_url");
        expectedCamera.setValue("test_value");
        expectedCamera.setTtl(1);
        when(connectionService.getJsonFromUrl(sourceDataUrl)).thenReturn("{\n" +
                "    \"urlType\": \"LIVE\",\n" +
                "    \"videoUrl\": \"test_video_url\"\n" +
                "}");
        when(connectionService.getJsonFromUrl(tokenDataUrl)).thenReturn("{\n" +
                "    \"value\": \"test_value\",\n" +
                "    \"ttl\": 1\n" +
                "}");
        CameraRequest cameraRequest = new CameraRequest();
        cameraRequest.setId(1);
        cameraRequest.setSourceDataUrl(sourceDataUrl);
        cameraRequest.setTokenDataUrl(tokenDataUrl);

        Camera camera = subj.getCameraFromRequest(cameraRequest);
        assertEquals(camera, expectedCamera);
        verify(connectionService, times(2)).getJsonFromUrl(any(String.class));
    }

    @Test
    public void getCameraFromRequest_wrong_data_in_request() throws IOException {
        String sourceDataUrl = "sourceDataUrl";
        String tokenDataUrl = "tokenDataUrl";

        when(connectionService.getJsonFromUrl(sourceDataUrl)).thenReturn("{\n" +
                "    \"urlType\": \"wrong_data\",\n" +
                "    \"videoUrl\": \"test_video_url\"\n" +
                "}");
        when(connectionService.getJsonFromUrl(tokenDataUrl)).thenReturn("{\n" +
                "    \"value\": \"test_value\",\n" +
                "    \"ttl\": 1\n" +
                "}");
        CameraRequest cameraRequest = new CameraRequest();
        cameraRequest.setId(1);
        cameraRequest.setSourceDataUrl(sourceDataUrl);
        cameraRequest.setTokenDataUrl(tokenDataUrl);

        assertThrows(IllegalArgumentException.class, () -> subj.getCameraFromRequest(cameraRequest));
    }

    @Test
    public void getCameraFromRequest_wrong_request() throws IOException {
        String sourceDataUrl = "sourceDataUrl";
        String tokenDataUrl = "tokenDataUrl";

        when(connectionService.getJsonFromUrl(anyString())).thenReturn("abracadabra");
        CameraRequest cameraRequest = new CameraRequest();
        cameraRequest.setId(1);
        cameraRequest.setSourceDataUrl(sourceDataUrl);
        cameraRequest.setTokenDataUrl(tokenDataUrl);

        assertThrows(IOException.class, () -> subj.getCameraFromRequest(cameraRequest));
    }

    @Test
    public void getCameraFromRequest_wrong_url() throws IOException {
        String sourceDataUrl = "sourceDataUrl";
        String tokenDataUrl = "tokenDataUrl";

        when(connectionService.getJsonFromUrl(anyString())).thenThrow(new IOException());
        CameraRequest cameraRequest = new CameraRequest();
        cameraRequest.setId(1);
        cameraRequest.setSourceDataUrl(sourceDataUrl);
        cameraRequest.setTokenDataUrl(tokenDataUrl);

        assertThrows(IOException.class, () -> subj.getCameraFromRequest(cameraRequest));
    }
}