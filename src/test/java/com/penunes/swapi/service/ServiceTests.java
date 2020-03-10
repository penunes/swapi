package com.penunes.swapi.service;

import com.penunes.swapi.client.SwapiClient;
import com.penunes.swapi.exception.BadRequestException;
import com.penunes.swapi.exception.NotFoundException;
import com.penunes.swapi.model.Planet;
import com.penunes.swapi.model.SwapiPlanet;
import com.penunes.swapi.model.SwapiPlanetResult;
import com.penunes.swapi.repository.PlanetRepository;
import com.penunes.swapi.service.impl.PlanetServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
public class ServiceTests {
    @Mock
    private SwapiClient swapiClient;

    @Mock
    private PlanetRepository repository;

    @InjectMocks
    private PlanetServiceImpl service;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void savePlanetOk(){
        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetResult(), HttpStatus.OK);
        Mockito
                .when(swapiClient.searchPlanetByName(Mockito.anyString()))
                .thenReturn(myEntity);

        Mockito
                .when(repository.findByName(Mockito.anyString()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.save(Mockito.any()))
                .thenReturn(new Planet());

        Planet response = service.save(getMockedPlanet());

        Assert.assertNotNull(response);
    }

    @Test(expected = BadRequestException.class)
    public void savePlanetAlreadyExists(){
        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetNoResult(), HttpStatus.OK);
        Mockito
                .when(swapiClient.searchPlanetByName(Mockito.anyString()))
                .thenReturn(myEntity);

        Mockito
                .when(repository.findByName(Mockito.anyString()))
                .thenReturn(getMockedPlanetList());

        service.save(getMockedPlanet());
    }

    @Test(expected = NotFoundException.class)
    public void savePlanetNoPlanetFound(){
        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetNoResult(), HttpStatus.OK);
        Mockito
                .when(swapiClient.searchPlanetByName(Mockito.anyString()))
                .thenReturn(myEntity);

        service.save(getMockedPlanet());
    }

    @Test(expected = BadRequestException.class)
    public void savePlanetBadRequest(){
        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetManyResults(), HttpStatus.OK);
        Mockito
                .when(swapiClient.searchPlanetByName(Mockito.anyString()))
                .thenReturn(myEntity);

        service.save(getMockedPlanet());
    }

    @Test
    public void getAllPlanetsOk(){
        Mockito
                .when(repository.findAll())
                .thenReturn(getMockedPlanetList());

        List<Planet> response = service.getPlanets();

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsFromUniverseOk(){
        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetResult(), HttpStatus.OK);
        ResponseEntity<SwapiPlanetResult> myEntityEndResponse = new ResponseEntity<>(getMockedSwapiPlanetResultEndResponse(), HttpStatus.OK);

        Mockito
                .when(swapiClient.getPlanetsFromUniverse(Mockito.anyInt()))
                .thenReturn(myEntity).thenReturn(myEntityEndResponse);

        List<Planet> response = service.getPlanetsFromUniverse();

        Assert.assertNotNull(response);
    }

    @Test
    public void getPlanetsByNameOk(){
        Mockito
                .when(repository.findByName(Mockito.anyString()))
                .thenReturn(getMockedPlanetList());
        List<Planet> response = service.getPlanetsByName("Earth");

        Assert.assertNotNull(response);
    }

    @Test(expected = BadRequestException.class)
    public void getPlanetsByNameBadRequest(){

        service.getPlanetsByName("Earth_quake");
    }

    @Test
    public void getPlanetByIdOk(){
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(getMockedOptionalPlanet());
        Planet response = service.getPlanetById(1L);

        Assert.assertNotNull(response);
    }

    @Test(expected = NotFoundException.class)
    public void getPlanetByIdNotFound(){
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(getMockedEmptyOptionalPlanet());
        Planet response = service.getPlanetById(2L);

        Assert.assertNotNull(response);
    }

//    @Test(expected = BadRequestException.class)
//    public void getPlanetByIdBadRequest(){
//
//        service.getPlanetById("Earth_quake");
//    }
//
//    @Test(expected = BadRequestException.class)
//    public void getPlanetByIdBadRequestInvalidHexValue(){
//
//        service.getPlanetById("Earthquake");
//    }

    @Test
    public void removePlanetOk(){
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(getMockedOptionalPlanet());

        Mockito.doNothing().when(repository).deleteById(Mockito.anyLong());
        service.removePlanetFrom(3L);
    }

    @Test(expected = NotFoundException.class)
    public void removePlanetNotFound(){
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenThrow(new NotFoundException("Planet Not Found"));

        service.removePlanetFrom(4L);
    }

    private SwapiPlanetResult getMockedSwapiPlanetResult(){
        SwapiPlanetResult result = new SwapiPlanetResult();
        List<SwapiPlanet> planets = new ArrayList<>();
        SwapiPlanet planet = new SwapiPlanet();

        planet.setName("Planet Test");
        planet.setClimate("temperate");
        planet.setTerrain("jungle");
        planet.setFilms(new ArrayList<>());

        planets.add(planet);
        result.setResults(planets);
        result.setNext("page=2");

        return result;
    }

    private SwapiPlanetResult getMockedSwapiPlanetResultEndResponse(){
        SwapiPlanetResult result = new SwapiPlanetResult();
        List<SwapiPlanet> planets = new ArrayList<>();
        SwapiPlanet planet = new SwapiPlanet();

        planet.setName("Planet Test");
        planet.setClimate("temperate");
        planet.setTerrain("jungle");
        planet.setFilms(new ArrayList<>());

        planets.add(planet);
        result.setResults(planets);
        result.setNext(null);

        return result;
    }

    private SwapiPlanetResult getMockedSwapiPlanetManyResults(){
        SwapiPlanetResult result = new SwapiPlanetResult();
        List<SwapiPlanet> planets = new ArrayList<>();
        for(int i = 0; i < 2 ; i++){
            SwapiPlanet planet = new SwapiPlanet();

            planet.setName("Planet Test");
            planet.setClimate("temperate");
            planet.setTerrain("jungle");
            planet.setFilms(new ArrayList<>());

            planets.add(planet);
        }

        result.setResults(planets);

        return result;
    }

    private SwapiPlanetResult getMockedSwapiPlanetNoResult(){
        SwapiPlanetResult result = new SwapiPlanetResult();
        List<SwapiPlanet> planets = new ArrayList<>();

        result.setResults(planets);

        return result;
    }

    Planet getMockedPlanet(){
        Planet planet = new Planet();
        planet.setName("Planet");
        planet.setClimate("temperate");
        planet.setTerrain("jungle");

        return planet;
    }

    Optional<Planet> getMockedOptionalPlanet(){
        return Optional.of(getMockedPlanet());
    }

    Optional<Planet> getMockedEmptyOptionalPlanet(){
        return Optional.empty();
    }

    List<Planet> getMockedPlanetList(){
        List<Planet> planets = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            planets.add(getMockedPlanet());
        }

        return planets;
    }
}