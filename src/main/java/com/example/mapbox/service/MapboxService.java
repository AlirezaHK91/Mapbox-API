package com.example.mapbox.service;

import com.example.mapbox.repository.CoordinateRepository;
import com.example.mapbox.model.Coordinate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapboxService {

    @Value("${mapbox.access-token}")
    private String accessToken;
    private static final String MAPBOX_API_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/{location}.json?access_token={accessToken}";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public void fetchAndSaveCoordinates(String location) throws Exception {
        // Call the Mapbox API using the injected access token
        String response = restTemplate.getForObject(MAPBOX_API_URL, String.class, location, accessToken);

        // Parse the JSON response to Json tree structure
        JsonNode jsonNode = objectMapper.readTree(response);

        // Extract coordinates
        JsonNode coordinatesNode = jsonNode.path("features").get(0).path("geometry").path("coordinates");

        double longitude = coordinatesNode.get(0).asDouble();
        double latitude = coordinatesNode.get(1).asDouble();

        // Save the coordinates to the database
        Coordinate coordinate = new Coordinate(latitude, longitude, location);
        coordinateRepository.save(coordinate);
    }
}