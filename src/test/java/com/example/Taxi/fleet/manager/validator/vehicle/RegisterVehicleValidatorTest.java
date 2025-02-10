package com.example.Taxi.fleet.manager.validator.vehicle;

import com.example.Taxi.fleet.manager.base.ValidationException;
import com.example.Taxi.fleet.manager.vehicle.validator.RegisterVehicleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.VehicleRequestModel;

import static com.example.Taxi.fleet.manager.base.vehicleBase.createVehicleRequestModel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterVehicleValidatorTest {

    private RegisterVehicleValidator validator;

    @BeforeEach
    void setUp() {
        validator = new RegisterVehicleValidator();
    }

    @Test
    @DisplayName("Given valid vehicle request, When validate is called, Then no exception is thrown")
    void givenValidVehicleRequest_whenValidate_thenNoException() {
        // Given
        VehicleRequestModel validRequest = createVehicleRequestModel();

        // When - Then
        validator.validate(validRequest);
    }

    @Test
    @DisplayName("Given null vehicle request, When validate is called, Then ValidationException is thrown")
    void givenNullVehicleRequest_whenValidate_thenThrowValidationException() {
        // Given
        VehicleRequestModel nullRequest = null;

        // When - Then
        assertThatThrownBy(() -> validator.validate(nullRequest))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("VehicleModel cannot be null")
                .extracting(exception -> ((ValidationException) exception).getErrors())
                .satisfies(errors -> assertThat(errors).containsExactly("VehicleModel is required"));
    }

    @Test
    @DisplayName("Given zero passenger capacity, When validate is called, Then ValidationException is thrown")
    void givenZeroPassengerCapacity_whenValidate_thenThrowValidationException() {
        // Given
        VehicleRequestModel request = createVehicleRequestModel();
        request.setPassengerCapacity(0);

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(request)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Passenger capacity must be at least 1");
    }

    @Test
    @DisplayName("Given negative range, When validate is called, Then ValidationException is thrown")
    void givenNegativeRange_whenValidate_thenThrowValidationException() {
        // Given
        VehicleRequestModel request = createVehicleRequestModel();
        request.setRangeKm(-50d);

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(request)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Range must be greater than 0");
    }

    @Test
    @DisplayName("Given null fuel type, When validate is called, Then ValidationException is thrown")
    void givenNullFuelType_whenValidate_thenThrowValidationException() {
        // Given
        VehicleRequestModel request = createVehicleRequestModel();
        request.setFuelType(null);

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(request)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Invalid fuel type provided");
    }

    @Test
    @DisplayName("Given null available type, When validate is called, Then ValidationException is thrown")
    void givenNullAvailableType_whenValidate_thenThrowValidationException() {
        // Given
        VehicleRequestModel request = createVehicleRequestModel();
        request.setAvailableType(null);

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(request)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Invalid available type provided");
    }
}
