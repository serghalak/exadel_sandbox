package com.exadel.sandbox.controllers.location_controller;

import com.exadel.sandbox.security.utill.JwtUtil;
import com.exadel.sandbox.service.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/location")
public class CityController {

    private final CityService cityService;
    private final ModelMapper mapper;
    private final JwtUtil jwtUtil;

    @Autowired
    public CityController(CityService cityService, ModelMapper mapper, JwtUtil jwtUtil) {
        this.cityService = cityService;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(produces = {"application/json"}, path = "/allCities")
    public ResponseEntity<?> getAllCity() {
        return new ResponseEntity<>(cityService.findAll(), HttpStatus.OK);
    }

    @GetMapping(produces = {"application/json"}, path = "/allActiveCities")
    public ResponseEntity<?> findCitiesByEventStatusActive() {
        return ResponseEntity.ok()
                .body(cityService.findCitiesByEventStatusActive());
    }

    @GetMapping(produces = {"application/json"}, path = "/allFavoriteCities")
    public ResponseEntity<?> findCitiesByFavoriteEvent() {
        Long userId = 2L;
        var cities = cityService.findCitiesByFavoriteEvent(userId);
        return ResponseEntity.ok()
                .body(cities);
    }

    @GetMapping(produces = {"application/json"}, path = "/allCitiesByCountry")
    public ResponseEntity<?> getAllCityByCountry(@RequestParam(name = "countryName") String countryName) {
        return new ResponseEntity<>(cityService.findCitiesByCountryNameOrderByName(countryName), HttpStatus.OK);
    }

}