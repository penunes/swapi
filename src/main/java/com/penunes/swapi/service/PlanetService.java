package com.penunes.swapi.service;

import com.penunes.swapi.model.Planet;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlanetService {
    Planet save(Planet planet);
    List<Planet> getPlanets();
    List<Planet> getPlanetsFromUniverse();
    List<Planet> getPlanetsByName(String name);
    Planet getPlanetById(Long id);
    void removePlanetFrom(Long id);
}