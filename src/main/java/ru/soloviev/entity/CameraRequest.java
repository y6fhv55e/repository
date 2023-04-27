package ru.soloviev.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CameraRequest {
    private int id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}