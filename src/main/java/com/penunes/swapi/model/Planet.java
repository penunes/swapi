package com.penunes.swapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.penunes.swapi.model.views.Views;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Data
public class Planet {
    @Id
    @JsonView(Views.Internal.class)
    private String id;

    @NotBlank
    @JsonView(Views.Public.class)
    private String name;

    @NotBlank
    @JsonView(Views.Public.class)
    private String climate;

    @NotBlank
    @JsonView(Views.Public.class)
    private String terrain;

    @JsonProperty("appearance_films")
    @JsonView(Views.Public.class)
    private Integer appearanceInFilms;
}