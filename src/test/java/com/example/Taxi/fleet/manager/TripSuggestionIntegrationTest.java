package com.example.Taxi.fleet.manager;

import com.example.Taxi.fleet.manager.TaxiFleetManagerApplication;
import com.example.Taxi.fleet.manager.vehicle.entity.Vehicle;
import com.example.Taxi.fleet.manager.vehicle.enums.AvailabilityStatus;
import com.example.Taxi.fleet.manager.vehicle.enums.FuelType;
import com.example.Taxi.fleet.manager.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicle;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TripSuggestionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();
    }

    @Test
    @DisplayName("Given valid parameters, When requesting trip suggestions, Then return a valid response")
    void givenValidParameters_whenGetTripSuggestions_thenReturnSuccess() throws Exception {
        // Given
        int passengers = 3;
        double distance = 100.5;
        final int DRIVER = 1;
        Vehicle vehicle = createVehicle();
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // When
        ResultActions response = mockMvc.perform(get("/trips/suggestions")
                .param("passengers", String.valueOf(passengers))
                .param("distance", String.valueOf(distance)));

        // Then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].vehicleId", is(savedVehicle.getId().toString())))
                .andExpect(jsonPath("$[0].passengerCapacity", is(savedVehicle.getPassengerCapacity() - DRIVER)))
                .andExpect(jsonPath("$[0].rangeKm", is(savedVehicle.getRangeKm())))
                .andExpect(jsonPath("$[0].fuelType", is(savedVehicle.getFuelType().name().toLowerCase())))
                .andExpect(jsonPath("$[0].profit.amount", greaterThan(0.0)))
                .andExpect(jsonPath("$[0].profit.currency", is("EUR")));
    }

    @Test
    @DisplayName("Given invalid passenger count, When requesting trip suggestions, Then return bad request")
    void givenInvalidPassengerCount_whenGetTripSuggestions_thenReturnBadRequest() throws Exception {
        // Given
        int invalidPassengers = 0;
        double distance = 100.5;

        // When
        ResultActions response = mockMvc.perform(get("/trips/suggestions")
                .param("passengers", String.valueOf(invalidPassengers))
                .param("distance", String.valueOf(distance)));

        // Then
        response.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given negative distance, When requesting trip suggestions, Then return bad request")
    void givenNegativeDistance_whenGetTripSuggestions_thenReturnBadRequest() throws Exception {
        // Given
        int passengers = 3;
        double invalidDistance = -10.0;

        // When
        ResultActions response = mockMvc.perform(get("/trips/suggestions")
                .param("passengers", String.valueOf(passengers))
                .param("distance", String.valueOf(invalidDistance)));

        // Then
        response.andExpect(status().isBadRequest());
    }
}
