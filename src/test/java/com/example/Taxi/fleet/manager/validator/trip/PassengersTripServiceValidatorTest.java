package com.example.Taxi.fleet.manager.validator.trip;


import com.example.Taxi.fleet.manager.base.ValidationException;
import com.example.Taxi.fleet.manager.trip.validator.PassengersTripServiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassengersTripServiceValidatorTest {

    private PassengersTripServiceValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PassengersTripServiceValidator();
    }

    @Test
    @DisplayName("Given valid parameters, When validate is called, Then no exception is thrown")
    void givenValidParameters_whenValidate_thenNoExceptionThrown() {
        // Given
        Integer passengers = 3;
        Double distance = 50.0;

        // When - Then
        validator.validate(passengers, distance);
    }

    @Test
    @DisplayName("Given null passengers, When validate is called, Then throw ValidationException")
    void givenNullPassengers_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = null;
        Double distance = 50.0;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Passenger count must be at least 1");
    }

    @Test
    @DisplayName("Given zero passengers, When validate is called, Then throw ValidationException")
    void givenZeroPassengers_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = 0;
        Double distance = 50.0;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Passenger count must be at least 1");
    }

    @Test
    @DisplayName("Given negative passengers, When validate is called, Then throw ValidationException")
    void givenNegativePassengers_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = -5;
        Double distance = 50.0;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Passenger count must be at least 1");
    }

    @Test
    @DisplayName("Given null distance, When validate is called, Then throw ValidationException")
    void givenNullDistance_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = 3;
        Double distance = null;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Distance must be greater than 0");
    }

    @Test
    @DisplayName("Given zero distance, When validate is called, Then throw ValidationException")
    void givenZeroDistance_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = 3;
        Double distance = 0.0;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Distance must be greater than 0");
    }

    @Test
    @DisplayName("Given negative distance, When validate is called, Then throw ValidationException")
    void givenNegativeDistance_whenValidate_thenThrowValidationException() {
        // Given
        Integer passengers = 3;
        Double distance = -20.0;

        // When
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> validator.validate(passengers, distance)
        );

        // Then
        assertThat(exception.getErrors()).containsExactly("Distance must be greater than 0");
    }
}

