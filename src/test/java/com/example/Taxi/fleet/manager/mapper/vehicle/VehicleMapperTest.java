package com.example.Taxi.fleet.manager.vehicle.mapper;

import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.enums.FuelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.AvailableTypeModel;
import org.openapitools.model.FuelTypeModel;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicle;
import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicleRequestModel;
import static org.assertj.core.api.Assertions.assertThat;

class VehicleMapperTest {

    private final VehicleMapper vehicleMapper = new VehicleMapper();

    @Test
    @DisplayName("Given a Vehicle entity, When mapped to VehicleResponseModel, Then all fields should match")
    void givenVehicleEntity_whenMappedToResponseModel_thenFieldsMatch() {
        // Given
        Vehicle vehicle = createVehicle();

        // When
        VehicleResponseModel responseModel = vehicleMapper.entityToResponseModel(vehicle);

        // Then
        assertThat(responseModel).isNotNull();
        assertThat(responseModel.getPassengerCapacity()).isEqualTo(vehicle.getPassengerCapacity());
        assertThat(responseModel.getRangeKm()).isEqualTo(vehicle.getRangeKm());
        assertThat(responseModel.getFuelType().name()).isEqualTo(vehicle.getFuelType().name());
        assertThat(responseModel.getAvailableType().name()).isEqualTo(vehicle.getAvailabilityStatus().name());
    }

    @Test
    @DisplayName("Given a VehicleRequestModel, When mapped to Vehicle entity, Then all fields should match")
    void givenVehicleRequestModel_whenMappedToEntity_thenFieldsMatch() {
        // Given
        VehicleRequestModel requestModel = createVehicleRequestModel();

        // When
        Vehicle vehicle = vehicleMapper.requestModelToEntity(requestModel);

        // Then
        assertThat(vehicle).isNotNull();
        assertThat(vehicle.getPassengerCapacity()).isEqualTo(requestModel.getPassengerCapacity());
        assertThat(vehicle.getRangeKm()).isEqualTo(requestModel.getRangeKm());
        assertThat(vehicle.getFuelType().name()).isEqualTo(requestModel.getFuelType().name());
        assertThat(vehicle.getAvailabilityStatus().name()).isEqualTo(requestModel.getAvailableType().name());
    }

    @Test
    @DisplayName("Given a list of Vehicle entities, When mapped to a list of VehicleResponseModels, Then all fields should match")
    void givenVehicleEntityList_whenMappedToResponseModelList_thenFieldsMatch() {
        // Given
        Vehicle vehicle1 = createVehicle();
        Vehicle vehicle2 = createVehicle();
        vehicle2.setFuelType(FuelType.GASOLINE);
        vehicle2.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);

        // When
        var responseList = vehicleMapper.entityListToResponseModelList(java.util.List.of(vehicle1, vehicle2));

        // Then
        assertThat(responseList).hasSize(2);
        assertThat(responseList.get(0).getFuelType()).isEqualTo(FuelTypeModel.ELECTRIC);
        assertThat(responseList.get(1).getFuelType()).isEqualTo(FuelTypeModel.GASOLINE);
        assertThat(responseList.get(1).getAvailableType()).isEqualTo(AvailableTypeModel.UNAVAILABLE);
    }
}
