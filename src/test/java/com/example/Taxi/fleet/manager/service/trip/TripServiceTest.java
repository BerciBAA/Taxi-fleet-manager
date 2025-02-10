package com.example.Taxi.fleet.manager.service.trip;

import com.example.Taxi.fleet.manager.trip.service.TripService;
import com.example.Taxi.fleet.manager.trip.validator.PassengersTripServiceValidator;
import com.example.Taxi.fleet.manager.vehicle.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.*;

import java.util.List;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicleResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private VehicleService vehicleService;

    @Mock
    private PassengersTripServiceValidator passengersTripServiceValidator;

    @InjectMocks
    private TripService tripService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given valid input, When requesting trip suggestions, Then return sorted valid suggestions")
    void givenValidInput_whenSuggestVehiclesForTrip_thenReturnSortedValidSuggestions() {
        // Given
        int passengers = 2;
        double distance = 80.0;
        VehicleResponseModel electricVehicle = createVehicleResponse(4, 100.0, FuelTypeModel.ELECTRIC);
        VehicleResponseModel gasolineVehicle = createVehicleResponse(6, 120.0, FuelTypeModel.GASOLINE);
        when(vehicleService.getAllVehicles()).thenReturn(List.of(electricVehicle, gasolineVehicle));

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).isNotEmpty();
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getProfit().getAmount()).isGreaterThan(response.get(1).getProfit().getAmount());
    }

    @Test
    @DisplayName("Given no available vehicles, When requesting trip suggestions, Then return empty list")
    void givenNoAvailableVehicles_whenSuggestVehiclesForTrip_thenReturnEmptyList() {
        // Given
        int passengers = 2;
        double distance = 80.0;
        when(vehicleService.getAllVehicles()).thenReturn(List.of());

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Given vehicles without sufficient range, When requesting trip suggestions, Then return empty list")
    void givenVehiclesWithoutSufficientRange_whenSuggestVehiclesForTrip_thenReturnEmptyList() {
        // Given
        int passengers = 2;
        double distance = 200.0;
        VehicleResponseModel vehicle = createVehicleResponse(4, 100.0, FuelTypeModel.ELECTRIC);
        when(vehicleService.getAllVehicles()).thenReturn(List.of(vehicle));

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    @DisplayName("Given electric vehicle, When requesting trip suggestions, Then return correct profit")
    void givenElectricVehicle_whenSuggestVehiclesForTrip_thenReturnCorrectProfit() {
        // Given
        int passengers = 2;
        double distance = 50.0;
        VehicleResponseModel electricVehicle = createVehicleResponse(4, 100.0, FuelTypeModel.ELECTRIC);
        when(vehicleService.getAllVehicles()).thenReturn(List.of(electricVehicle));

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).hasSize(1);
        TripSuggestionResponseModel suggestion = response.getFirst();
        assertThat(suggestion.getProfit().getAmount()).isEqualTo(166.0);
    }

    @Test
    @DisplayName("Given gasoline vehicle, When requesting trip suggestions, Then return correct profit")
    void givenGasolineVehicle_whenSuggestVehiclesForTrip_thenReturnCorrectProfit() {
        // Given
        int passengers = 2;
        double distance = 50.0;
        VehicleResponseModel gasolineVehicle = createVehicleResponse(4, 100.0, FuelTypeModel.GASOLINE);
        when(vehicleService.getAllVehicles()).thenReturn(List.of(gasolineVehicle));

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).hasSize(1);
        TripSuggestionResponseModel suggestion = response.getFirst();
        assertThat(suggestion.getProfit().getAmount()).isEqualTo(116.0);
    }

    @Test
    @DisplayName("Given hybrid vehicle, When requesting trip suggestions, Then return correct profit")
    void givenHybridVehicle_whenSuggestVehiclesForTrip_thenReturnCorrectProfit() {
        // Given
        int passengers = 2;
        double distance = 50.0;
        VehicleResponseModel hybridVehicle = createVehicleResponse( 4, 100.0, FuelTypeModel.HYBRID);
        when(vehicleService.getAllVehicles()).thenReturn(List.of(hybridVehicle));

        // When
        List<TripSuggestionResponseModel> response = tripService.suggestVehiclesForTrip(passengers, distance);

        // Then
        assertThat(response).hasSize(1);
        TripSuggestionResponseModel suggestion = response.getFirst();
        assertThat(suggestion.getProfit().getAmount()).isEqualTo(191.0);
    }
}