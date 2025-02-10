package com.example.Taxi.fleet.manager.vehicle.validator;

import com.example.Taxi.fleet.manager.base.ValidationException;
import com.example.Taxi.fleet.manager.base.ModelValidator;
import jakarta.validation.Valid;
import org.openapitools.model.AvailableTypeModel;
import org.openapitools.model.FuelTypeModel;
import org.openapitools.model.VehicleRequestModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegisterVehicleValidator implements ModelValidator<VehicleRequestModel> {

    @Override
    public void validate(VehicleRequestModel vehicleModel) {
        List<String> validationErrors = new ArrayList<>();

        if (vehicleModel == null) {
            throw new ValidationException("VehicleModel cannot be null", List.of("VehicleModel is required"));
        }

        if (vehicleModel.getPassengerCapacity() < 1) {
            validationErrors.add("Passenger capacity must be at least 1");
        }

        if (vehicleModel.getRangeKm() == null || vehicleModel.getRangeKm() <= 0) {
            validationErrors.add("Range must be greater than 0");
        }

        if (!isValidFuelType(vehicleModel.getFuelType())) {
            validationErrors.add("Invalid fuel type provided");
        }

        if (!isValidAvailableType(vehicleModel.getAvailableType())) {
            validationErrors.add("Invalid available type provided");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation failed for VehicleModel", validationErrors);
        }
    }

    private boolean isValidAvailableType(AvailableTypeModel availableType) {
        try {
            AvailableTypeModel.valueOf(availableType.name());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }

    private boolean isValidFuelType(FuelTypeModel fuelTypeModel) {
        try {
            FuelTypeModel.valueOf(fuelTypeModel.name());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}

