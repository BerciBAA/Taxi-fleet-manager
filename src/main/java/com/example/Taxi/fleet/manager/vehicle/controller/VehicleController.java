package com.example.Taxi.fleet.manager.vehicle.controller;

import com.example.Taxi.fleet.manager.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.VehiclesApi;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class VehicleController implements VehiclesApi {

    private final VehicleService vehicleService;

    @Override
    public ResponseEntity<VehicleResponseModel> registerVehicle(VehicleRequestModel vehicleRequestModel) {
        return ResponseEntity.status(201).body(vehicleService.registerVehicle(vehicleRequestModel));
    }

    @Override
    public ResponseEntity<List<VehicleResponseModel>> getAllVehicles() {
        return ResponseEntity.ok().body(vehicleService.getAllVehicles());
    }
}
