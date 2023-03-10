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
public class Location {
    @EqualsAndHashCode.Exclude
    @Id
    private float lat;
    @Id
    private float lon;
}