package com.indianoil.VisitorManagement.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indianoil.VisitorManagement.entity.LocationEntity;
import com.indianoil.VisitorManagement.interfaces.Divisions;
import com.indianoil.VisitorManagement.interfaces.Locations;
import com.indianoil.VisitorManagement.services.LocationService;

@RestController
@EnableAutoConfiguration

@RequestMapping("api/v1/location")
@Controller
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<List<Divisions>> state() throws IOException {
        List<Divisions> divisions = locationService.getAllDivisions();
        return ResponseEntity.ok().body(divisions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Locations>> city(@PathVariable("id") String id) throws IOException {
        List<Locations> locations = locationService.getAllLocations(id);
        return ResponseEntity.ok().body(locations);
    }
}
