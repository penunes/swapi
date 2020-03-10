package com.penunes.swapi.client;

import com.penunes.swapi.model.SwapiPlanetResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class SwapiClient {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${swapi.baseurl}")
    private String URL;

    @Value("${swapi.planet.path}")
    private String PATH;

    public ResponseEntity<SwapiPlanetResult> searchPlanetByName(String name){
        String searchString = "?search=" + name;
        return doGetRequest(searchString);
    }

    public ResponseEntity<SwapiPlanetResult> getPlanetsFromUniverse(Integer page){
        String pageString = "";

        if(page != null && page > 0){
            pageString = "?page=" + page;
        }

        return doGetRequest(pageString);
    }

    private ResponseEntity<SwapiPlanetResult> doGetRequest(String queryString) {
        try {
            return restTemplate.exchange(URL + PATH + queryString,
                    HttpMethod.GET,
                    new HttpEntity<Void>(null, null),
                    SwapiPlanetResult.class);
        } catch (HttpClientErrorException exc) {
            throw exc;
        }
    }
}