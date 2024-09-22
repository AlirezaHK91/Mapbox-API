package com.example.mapbox.controller;

import com.example.mapbox.service.MapboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapboxController {

    @Autowired
    private MapboxService mapboxService;

    @GetMapping("/fetch-coordinates")
    public String fetchCoordinates(@RequestParam String location) {
        try {
            mapboxService.fetchAndSaveCoordinates(location);
            return "Coordinates fetched and saved successfully!";
        } catch (Exception e) {
            return "Error fetching coordinates: " + e.getMessage();
        }
    }
}