package com.example.Taxi.fleet.manager.vehicle.service;

import com.example.Taxi.fleet.manager.base.ModelValidator;
import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.mapper.VehicleMapper;
import com.example.Taxi.fleet.manager.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final ModelValidator<VehicleRequestModel> vehicleValidator;

    public VehicleResponseModel registerVehicle(VehicleRequestModel vehicleRequestModel) {
        log.info("Registering a new vehicle: {}", vehicleRequestModel);

        vehicleValidator.validate(vehicleRequestModel);

        Vehicle vehicle = vehicleMapper.requestModelToEntity(vehicleRequestModel);
        vehicle.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        VehicleResponseModel responseModel = vehicleMapper.entityToResponseModel(savedVehicle);

        log.info("Vehicle registered successfully with ID: {}", savedVehicle.getId());
        return responseModel;
    }

    public List<VehicleResponseModel> getAllVehicles() {
        log.info("Fetching all vehicles from the database.");

        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleResponseModel> responseModels = vehicleMapper.entityListToResponseModelList(vehicles);

        log.info("Retrieved {} vehicles from the database.", responseModels.size());
        return responseModels;
    }
}
