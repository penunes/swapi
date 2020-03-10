package com.penunes.swapi.client;

import com.penunes.swapi.model.SwapiPlanet;
import com.penunes.swapi.model.SwapiPlanetResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class SwapiClientTests {
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SwapiClient client;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void searchPlanetByNameOk(){
        ReflectionTestUtils.setField(client, "URL", "URL_TO_SWAPI");
        ReflectionTestUtils.setField(client, "PATH", "PATH_TO_PLANETS");

        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetResult(), HttpStatus.OK);

        Mockito
                .when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(HttpEntity.class),
                        Mockito.<Class<SwapiPlanetResult>>any()))
                .thenReturn(myEntity);

        ResponseEntity<SwapiPlanetResult> response = client.searchPlanetByName("name");

        Mockito
                .verify(restTemplate, Mockito.times(1)).exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.<Class<SwapiPlanetResult>>any());

        Assert.assertNotNull(response);
    }

    @Test(expected = HttpClientErrorException.class)
    public void searchPlanetByNameNotOk(){
        ReflectionTestUtils.setField(client, "URL", "URL_TO_SWAPI");
        ReflectionTestUtils.setField(client, "PATH", "PATH_TO_PLANETS");

        Mockito
                .when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(HttpEntity.class),
                        Mockito.<Class<SwapiPlanetResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Retrieving data"));

        client.searchPlanetByName("name");
    }

    @Test
    public void getPlanetsFromUniverseOk(){
        ReflectionTestUtils.setField(client, "URL", "URL_TO_SWAPI");
        ReflectionTestUtils.setField(client, "PATH", "PATH_TO_PLANETS");

        ResponseEntity<SwapiPlanetResult> myEntity = new ResponseEntity<>(getMockedSwapiPlanetResult(), HttpStatus.OK);

        Mockito
                .when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(HttpEntity.class),
                        Mockito.<Class<SwapiPlanetResult>>any()))
                .thenReturn(myEntity);

        ResponseEntity<SwapiPlanetResult> response = client.getPlanetsFromUniverse(Integer.valueOf(2));

        Mockito
                .verify(restTemplate, Mockito.times(1)).exchange(
                Mockito.any(String.class),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.<Class<SwapiPlanetResult>>any());

        Assert.assertNotNull(response);
    }

    @Test(expected = HttpClientErrorException.class)
    public void getPlanetsFromUniverseNotOk(){
        ReflectionTestUtils.setField(client, "URL", "URL_TO_SWAPI");
        ReflectionTestUtils.setField(client, "PATH", "PATH_TO_PLANETS");

        Mockito
                .when(restTemplate.exchange(
                        Mockito.any(String.class),
                        Mockito.any(HttpMethod.class),
                        Mockito.any(HttpEntity.class),
                        Mockito.<Class<SwapiPlanetResult>>any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error Retrieving data"));

        client.getPlanetsFromUniverse(Integer.valueOf(2));
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
}