package com.penunes.swapi.model;

import lombok.Data;

import java.util.List;

@Data
public class SwapiPlanetResult {
    private Integer count;
    private String next;
    private String previous;
    private List<SwapiPlanet> results;
}