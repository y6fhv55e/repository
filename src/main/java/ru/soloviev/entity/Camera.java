package ru.soloviev.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Setter
@NoArgsConstructor
public class Camera {
    private int id;
    private UrlType urlType;
    private String videoUrl;
    private String value;
    private int ttl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Camera)) return false;
        Camera camera = (Camera) o;
        return id == camera.id &&
                ttl == camera.ttl &&
                urlType == camera.urlType &&
                Objects.equals(videoUrl, camera.videoUrl) &&
                Objects.equals(value, camera.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlType, videoUrl, value, ttl);
    }

    @Override
    public String toString() {
        return "{\n" +
                "   \"id\": " + id + ",\n" +
                "   \"urlType\": \"" + urlType + "\",\n" +
                "   \"videoUrl\": \"" + videoUrl + "\",\n" +
                "   \"value\": \"" + value + "\",\n" +
                "   \"ttl\": " + ttl + "\n" +
                "}";
    }
}