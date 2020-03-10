package com.penunes.swapi.repository;

import com.penunes.swapi.model.Planet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, Long> {
    @Query(value = "{'name': {$regex : ?0, $options: 'i'}}")
    List<Planet> findByName(String name);
}