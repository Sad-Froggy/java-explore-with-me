package ru.practicum.events.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations", schema = "public")
@IdClass(LocationId.class)
public class Location {
    @Id
    private float lat;
    @Id
    private float lon;
}