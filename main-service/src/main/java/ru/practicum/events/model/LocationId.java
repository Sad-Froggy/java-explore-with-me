package ru.practicum.events.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationId implements Serializable {
    private float lat;
    private float lon;
}
