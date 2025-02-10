package com.example.Taxi.fleet.manager.trip.controller;

import com.example.Taxi.fleet.manager.trip.service.TripService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.TripsApi;
import org.openapitools.model.TripSuggestionResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TripController implements TripsApi {

    private final TripService tripService;

    @Override
    public ResponseEntity<List<TripSuggestionResponseModel>> suggestVehiclesForTrip(Integer passengers, Double distance) {
        return ResponseEntity.ok().body(tripService.suggestVehiclesForTrip(passengers, distance));
    }
}
