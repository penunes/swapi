package com.penunes.swapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.time.LocalDateTime;

@Data
public class SwapiPlanet {
    private String name;
    @JsonProperty("rotation_period")
    private String rotationPeriod;
    @JsonProperty("orbital_period")
    private String orbitalPeriod;
    private String diameter;
    private String climate;
    private String gravity;
    private String terrain;
    @JsonProperty("surface_water")
    private String surfaceWater;
    private String population;
    List<String> residents;
    List<String> films;
    private LocalDateTime created;
    private LocalDateTime edited;
    private String url;
}