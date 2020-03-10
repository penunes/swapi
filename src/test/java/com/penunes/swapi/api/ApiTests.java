package com.penunes.swapi.api;

import com.penunes.swapi.exception.NotFoundException;
import com.penunes.swapi.model.Planet;
import com.penunes.swapi.service.PlanetService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class ApiTests {
    @Mock
    private PlanetService service;

    @InjectMocks
    private SwapiApi controller;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createPlanetOk(){
        Planet mockedPlanet = getMockedPlanet();

        Mockito
                .when(service.save(Mockito.any()))
                .thenReturn(mockedPlanet);

        Planet response = controller.createPlanet(new Planet());

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsFromUniverseOk(){
        List<Planet> mockedPlanets = getMockedPlanetList();

        Mockito
                .when(service.getPlanetsFromUniverse())
                .thenReturn(mockedPlanets);

        List<Planet> response = controller.getPlanetsFromUniverse();

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsByNameOk(){
        List<Planet> mockedPlanets = getMockedPlanetList();

        Mockito
                .when(service.getPlanetsByName(Mockito.anyString()))
                .thenReturn(mockedPlanets);

        List<Planet> response = controller.getPlanetsByName(Optional.of("a122"));

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsByNameListAll(){
        List<Planet> mockedPlanets = getMockedPlanetList();
        Mockito
                .when(service.getPlanets())
                .thenReturn(mockedPlanets);

        List<Planet> response = controller.getPlanetsByName(Optional.empty());

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsByIdOk(){
        Planet mockedPlanet = getMockedPlanet();

        Mockito
                .when(service.getPlanetById(Mockito.anyLong()))
                .thenReturn(mockedPlanet);

        Planet response = controller.getPlanetById(5L);

        Assert.assertNotNull(response);
    }

    @Test
    public void deletePlanetOk(){
        Mockito.doNothing()
                .when(service).removePlanetFrom(Mockito.anyLong());

        controller.deletePlanetById(6L);
    }

    Planet getMockedPlanet(){
        Planet planet = new Planet();
        planet.setName("Planet");
        planet.setClimate("temperate");
        planet.setTerrain("jungle");

        return planet;
    }

    List<Planet> getMockedPlanetList(){
        List<Planet> planets = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            planets.add(getMockedPlanet());
        }

        return planets;
    }
}