package com.example.Taxi.fleet.manager.base;

import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.enums.FuelType;
import org.openapitools.model.*;

public class vehicleBase {

    public static Vehicle createVehicle(){
        Vehicle vehicle = new Vehicle();
        vehicle.setPassengerCapacity(5);
        vehicle.setRangeKm(200);
        vehicle.setFuelType(FuelType.ELECTRIC);
        vehicle.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        return vehicle;
    }

    public static VehicleRequestModel createVehicleRequestModel(){
        VehicleRequestModel vehicleRequestModel = new VehicleRequestModel();
        vehicleRequestModel.setPassengerCapacity(5);
        vehicleRequestModel.setRangeKm(200d);
        vehicleRequestModel.setFuelType(FuelTypeModel.ELECTRIC);
        vehicleRequestModel.setAvailableType(AvailableTypeModel.AVAILABLE);
        return vehicleRequestModel;
    }

    public static VehicleResponseModel createVehicleResponse(int capacity, double range, FuelTypeModel fuelType) {
        VehicleResponseModel vehicle = new VehicleResponseModel();
        vehicle.setPassengerCapacity(capacity);
        vehicle.setRangeKm(range);
        vehicle.setFuelType(fuelType);
        vehicle.setAvailableType(AvailableTypeModel.AVAILABLE);
        return vehicle;
    }


}
