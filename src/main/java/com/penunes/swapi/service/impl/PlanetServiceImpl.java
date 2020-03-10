package com.penunes.swapi.service.impl;

import com.penunes.swapi.client.SwapiClient;
import com.penunes.swapi.exception.NotFoundException;
import com.penunes.swapi.exception.BadRequestException;
import com.penunes.swapi.model.Planet;
import com.penunes.swapi.model.SwapiPlanet;
import com.penunes.swapi.model.SwapiPlanetResult;
import com.penunes.swapi.repository.PlanetRepository;
import com.penunes.swapi.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private SwapiClient swapiClient;

    @Autowired
    private PlanetRepository repository;

    @Value("${server.host}")
    private String host;

    @Value("${server.servlet.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${swapi.planet.path}")
    private String swapiPath;

    private final Integer ZERO = 0;
    private final Integer ONE = 1;

    @Override
    public Planet save(Planet planet){
        ResponseEntity<SwapiPlanetResult> responseFromUniverse = swapiClient.searchPlanetByName(planet.getName());
        SwapiPlanetResult planetsFromUniverse = null;
        if(responseFromUniverse.getStatusCode().equals(HttpStatus.OK)){
            planetsFromUniverse = responseFromUniverse.getBody();
        }

        if(planetsFromUniverse.getResults().isEmpty()){
            throw new NotFoundException("No planet found for name searched " + planet.getName());
        }

        if(!repository.findByName(planet.getName()).isEmpty()){
            throw new BadRequestException("Planet already exists -> " + planet.getName());
        }

        if(planetsFromUniverse.getResults().size() > ONE){
            List<String> names = new ArrayList<>();
            planetsFromUniverse.getResults().stream().forEach(result -> names.add(result.getName()));
            throw new BadRequestException("Please choose one name from list and resend request -> " + names);
        }

        SwapiPlanet planetFromUniverse = planetsFromUniverse.getResults().get(ZERO);
        planet.setAppearanceInFilms(planetFromUniverse.getFilms() != null ? planetFromUniverse.getFilms().size() : Integer.valueOf(ZERO));

        Long id = repository.count();
        planet.setId(++id);
        planet.setUrl(host + (port.isEmpty() ? "" : ":" + port) + contextPath + "/" + swapiPath + id);

        return repository.save(planet);
    }

    @Override
    public List<Planet> getPlanets(){
        return repository.findAll();
    }

    @Override
    public List<Planet> getPlanetsFromUniverse(){
        Integer page = 0;
        String nextPage = "";
        List<Planet> planets = new ArrayList<>();

        do{
            ResponseEntity<SwapiPlanetResult> responseFromUniverse = swapiClient.getPlanetsFromUniverse(page);
            if(responseFromUniverse.getStatusCode().equals(HttpStatus.OK)){
                fillPlanetList(responseFromUniverse, planets);
                nextPage = getNextPage(responseFromUniverse);
                if(nextPage != null){
                    String[] splitNextAttribute = nextPage.split("=");
                    if(splitNextAttribute.length > ZERO){
                        page = Integer.valueOf(splitNextAttribute[ONE]);
                    }
                }
            }

        }while(nextPage != null);

        return planets;
    }

    private void fillPlanetList(ResponseEntity<SwapiPlanetResult> responseFromUniverse, List<Planet> planets){
        addPlanetsToList(responseFromUniverse.getBody(), planets);
    }

    private void addPlanetsToList(SwapiPlanetResult responseFromUniverse, List<Planet> planets){
        responseFromUniverse.getResults().stream().forEach(planetFromUniverse -> {
            Planet planet = new Planet();
            planet.setName(planetFromUniverse.getName());
            planet.setClimate(planetFromUniverse.getClimate());
            planet.setTerrain(planetFromUniverse.getTerrain());
            planet.setAppearanceInFilms(planetFromUniverse.getFilms().size());
            planets.add(planet);
        });
    }

    private String getNextPage(ResponseEntity<SwapiPlanetResult> responseFromUniverse){
        return responseFromUniverse.getBody().getNext();
    }

    @Override
    public List<Planet> getPlanetsByName(String name){
        Pattern pattern = Pattern.compile("[^a-zA-Z]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);

        if(matcher.find()){
            throw new BadRequestException("Invalid name, special characters not allowed");
        }

        return repository.findByName(name);
    }

    @Override
    public Planet getPlanetById(Long id){
        Optional<Planet> dbEntity = repository.findById(id);
        if(dbEntity.isPresent()){
            return dbEntity.get();
        }else{
            throw new NotFoundException("Planet Not Found");
        }
    }

    @Override
    public void removePlanetFrom(Long id){
        getPlanetById(id);

        repository.deleteById(id);
    }
}