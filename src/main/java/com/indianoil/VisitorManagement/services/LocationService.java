package com.indianoil.VisitorManagement.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indianoil.VisitorManagement.interfaces.Divisions;
import com.indianoil.VisitorManagement.interfaces.Locations;
import com.indianoil.VisitorManagement.repository.LocationRepository;


@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    public List<Divisions> getAllDivisions() {
        return locationRepository.findAllDivisions();
    }

    public List<Locations> getAllLocations(String divCode) {
        return locationRepository.findAllLocations(divCode);
    }
}
