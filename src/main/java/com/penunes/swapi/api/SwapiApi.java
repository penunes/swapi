package com.penunes.swapi.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.penunes.swapi.model.Planet;
import com.penunes.swapi.model.views.Views;
import com.penunes.swapi.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping("/planets")
public class SwapiApi {

    @Autowired
    private PlanetService planetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(Views.Public.class)
    public Planet createPlanet(@Valid @RequestBody Planet planet){
        return planetService.save(planet);
    }

    @GetMapping("/from-universe")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public List<Planet> getPlanetsFromUniverse(){
        return planetService.getPlanetsFromUniverse();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public List<Planet> getPlanetsByName(@RequestParam Optional<String> name){
        if(name.isPresent()){
            return planetService.getPlanetsByName(name.get());
        }else{
            return planetService.getPlanets();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @JsonView(Views.Public.class)
    public Planet getPlanetById(@PathVariable Long id){
        return planetService.getPlanetById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlanetById(@PathVariable Long id){
        planetService.removePlanetFrom(id);
    }

}
