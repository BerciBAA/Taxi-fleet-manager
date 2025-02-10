package com.example.Taxi.fleet.manager.service.vehicle;

import com.example.Taxi.fleet.manager.base.ModelValidator;
import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.mapper.VehicleMapper;
import com.example.Taxi.fleet.manager.vehicle.repository.VehicleRepository;
import com.example.Taxi.fleet.manager.vehicle.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;

import java.util.List;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicle;
import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicleRequestModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private ModelValidator<VehicleRequestModel> vehicleValidator;

    @InjectMocks
    private VehicleService vehicleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given a valid vehicle request, When registering a vehicle, Then save and return the response model")
    void givenValidVehicleRequest_whenRegisterVehicle_thenSaveAndReturn() {
        // Given
        VehicleRequestModel requestModel = createVehicleRequestModel();
        Vehicle vehicle = createVehicle();
        VehicleResponseModel responseModel = new VehicleResponseModel();

        when(vehicleMapper.requestModelToEntity(requestModel)).thenReturn(vehicle);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        when(vehicleMapper.entityToResponseModel(vehicle)).thenReturn(responseModel);

        // When
        VehicleResponseModel result = vehicleService.registerVehicle(requestModel);

        // Then
        verify(vehicleValidator).validate(requestModel);
        verify(vehicleMapper).requestModelToEntity(requestModel);
        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).entityToResponseModel(vehicle);

        assertThat(result).isEqualTo(responseModel);
        assertThat(vehicle.getAvailabilityStatus()).isEqualTo(AvailabilityStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Given saved vehicles, When fetching all vehicles, Then return a list of response models")
    void givenSavedVehicles_whenGetAllVehicles_thenReturnList() {
        // Given
        List<Vehicle> vehicles = List.of(createVehicle());
        List<VehicleResponseModel> responseModels = List.of(new VehicleResponseModel());

        when(vehicleRepository.findAll()).thenReturn(vehicles);
        when(vehicleMapper.entityListToResponseModelList(vehicles)).thenReturn(responseModels);

        // When
        List<VehicleResponseModel> result = vehicleService.getAllVehicles();

        // Then
        verify(vehicleRepository).findAll();
        verify(vehicleMapper).entityListToResponseModelList(vehicles);

        assertThat(result).isEqualTo(responseModels);
    }

    @Test
    @DisplayName("Given a valid vehicle request, When registering a vehicle, Then ensure the availability is set to AVAILABLE")
    void givenValidVehicleRequest_whenRegisterVehicle_thenEnsureAvailabilityIsSet() {
        // Given
        VehicleRequestModel requestModel = createVehicleRequestModel();
        Vehicle vehicle = createVehicle();
        VehicleResponseModel responseModel = new VehicleResponseModel();

        when(vehicleMapper.requestModelToEntity(requestModel)).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(vehicleMapper.entityToResponseModel(vehicle)).thenReturn(responseModel);

        // When
        vehicleService.registerVehicle(requestModel);

        // Then
        ArgumentCaptor<Vehicle> vehicleCaptor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(vehicleCaptor.capture());

        Vehicle savedVehicle = vehicleCaptor.getValue();
        assertThat(savedVehicle.getAvailabilityStatus()).isEqualTo(AvailabilityStatus.AVAILABLE);
    }
}
