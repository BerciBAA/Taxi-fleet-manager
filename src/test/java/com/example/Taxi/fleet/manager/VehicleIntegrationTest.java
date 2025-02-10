package com.example.Taxi.fleet.manager;

import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.enums.FuelType;
import com.example.Taxi.fleet.manager.vehicle.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.model.VehicleRequestModel;
import org.openapitools.model.VehicleResponseModel;
import org.openapitools.model.FuelTypeModel;
import org.openapitools.model.AvailableTypeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicle;
import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicleRequestModel;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class VehicleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    @DisplayName("Given valid vehicle, When registering a new vehicle, Then return 201 Created")
    void givenValidVehicle_whenRegisterVehicle_thenReturnCreated() throws Exception {
        // Given
        VehicleRequestModel vehicleRequestModel = createVehicleRequestModel();

        // When
        ResultActions response = mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleRequestModel)));

        // Then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.passengerCapacity", is(vehicleRequestModel.getPassengerCapacity())))
                .andExpect(jsonPath("$.rangeKm", is(vehicleRequestModel.getRangeKm())))
                .andExpect(jsonPath("$.fuelType", is(vehicleRequestModel.getFuelType().getValue())))
                .andExpect(jsonPath("$.availableType", is(vehicleRequestModel.getAvailableType().getValue())));
    }

    @Test
    @DisplayName("Given invalid vehicle (negative range), When registering, Then return 400 Bad Request")
    void givenInvalidVehicle_whenRegisterVehicle_thenReturnBadRequest() throws Exception {
        // Given
        VehicleRequestModel invalidVehicle = createVehicleRequestModel();
        invalidVehicle.setRangeKm(-100.0);


        // When
        ResultActions response = mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidVehicle)));

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given valid request, When fetching all vehicles, Then return list of vehicles")
    void givenVehiclesExist_whenGetAllVehicles_thenReturnList() throws Exception {
        // Given
        Vehicle vehicle1 = createVehicle();
        vehicle1.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        Vehicle vehicle2 = createVehicle();
        vehicle2.setAvailabilityStatus(AvailabilityStatus.UNAVAILABLE);

        vehicleRepository.save(vehicle1);
        vehicleRepository.save(vehicle2);

        // When
        ResultActions response = mockMvc.perform(get("/vehicles"));

        // Then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].fuelType", is(vehicle1.getFuelType().name().toLowerCase())))
                .andExpect(jsonPath("$[1].availableType", is(vehicle2.getAvailabilityStatus().name().toLowerCase())));
    }
}
