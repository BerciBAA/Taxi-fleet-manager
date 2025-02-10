package com.example.Taxi.fleet.manager.vehicle.mapper;

import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.enums.FuelType;
import org.openapitools.model.AvailableTypeModel;
import org.openapitools.model.FuelTypeModel;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleMapper {

    public VehicleResponseModel entityToResponseModel(Vehicle source) {
        VehicleResponseModel target = new VehicleResponseModel();
        target.setId(source.getId());
        target.setPassengerCapacity(source.getPassengerCapacity());
        target.setRangeKm(source.getRangeKm());
        target.setFuelType(FuelTypeModel.valueOf(source.getFuelType().name()));
        target.setAvailableType(AvailableTypeModel.valueOf(source.getAvailabilityStatus().name()));
        return target;
    }

    public Vehicle requestModelToEntity(VehicleRequestModel source) {
        Vehicle target = new Vehicle();
        target.setPassengerCapacity(source.getPassengerCapacity());
        target.setRangeKm(source.getRangeKm());
        target.setFuelType(FuelType.valueOf(source.getFuelType().name()));
        target.setAvailabilityStatus(AvailabilityStatus.valueOf(source.getAvailableType().name()));
        return target;
    }

    public List<VehicleResponseModel> entityListToResponseModelList(List<Vehicle> sources) {
        return sources.stream().map(this::entityToResponseModel).toList();
    }
}
