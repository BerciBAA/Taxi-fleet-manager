package com.example.Taxi.fleet.manager.trip.validator;

import com.example.Taxi.fleet.manager.base.ValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PassengersTripServiceValidator {

    public void validate(Integer passengers, Double distance) {
        List<String> validationErrors = new ArrayList<>();

        if (passengers == null || passengers < 1) {
            validationErrors.add("Passenger count must be at least 1");
        }

        if (distance == null || distance <= 0d) {
            validationErrors.add("Distance must be greater than 0");
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException("Validation failed for Trip parameters", validationErrors);
        }
    }
}
