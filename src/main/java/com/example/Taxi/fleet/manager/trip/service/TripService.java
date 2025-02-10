package com.example.Taxi.fleet.manager.trip.service;

import com.example.Taxi.fleet.manager.trip.validator.PassengersTripServiceValidator;
import com.example.Taxi.fleet.manager.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.*;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TripService {

    private final VehicleService vehicleService;
    private final PassengersTripServiceValidator passengersTripServiceValidator;
    private final int DRIVER = 1;

    public List<TripSuggestionResponseModel> suggestVehiclesForTrip(Integer passengers, Double distance) {
        log.info("Received request to suggest vehicles for {} passengers and {} km distance.", passengers, distance);

        passengersTripServiceValidator.validate(passengers, distance);
        List<VehicleResponseModel> allVehicles = vehicleService.getAllVehicles();

        log.info("Found {} vehicles in the system. Filtering eligible vehicles...", allVehicles.size());

        List<TripSuggestionResponseModel> suggestions = allVehicles.stream()
                .filter(vehicle -> isVehicleEligibleForTrip(vehicle, passengers, distance))
                .map(vehicle -> createTripSuggestion(vehicle, passengers, distance))
                .sorted(Comparator.comparing(trip -> trip.getProfit().getAmount(), Comparator.reverseOrder()))
                .toList();

        log.info("Generated {} trip suggestions.", suggestions.size());
        return suggestions;
    }

    private TripSuggestionResponseModel createTripSuggestion(VehicleResponseModel vehicle, Integer passengers, Double distance) {
        log.debug("Creating trip suggestion for vehicle ID: {}", vehicle.getId());

        TripSuggestionResponseModel responseModel = new TripSuggestionResponseModel();
        responseModel.setVehicleId(vehicle.getId());
        responseModel.setProfit(createMonetaryAmount(calculateProfit(vehicle, passengers, distance)));
        responseModel.setPassengerCapacity(vehicle.getPassengerCapacity() - DRIVER);
        responseModel.setRangeKm(vehicle.getRangeKm());
        responseModel.setFuelType(vehicle.getFuelType());

        log.debug("Trip suggestion created: {}", responseModel);
        return responseModel;
    }

    private MonetaryAmountModel createMonetaryAmount(Double amount) {
        MonetaryAmountModel monetaryAmount = new MonetaryAmountModel();
        monetaryAmount.setAmount(amount);
        monetaryAmount.setCurrency(MonetaryAmountModel.CurrencyEnum.EUR);
        return monetaryAmount;
    }

    private Double calculateProfit(VehicleResponseModel vehicle, Integer passengers, Double distance) {
        Double travelFee = calculateTravelFee(passengers, distance);
        Double refuelingCost = calculateRefuelingCost(vehicle, distance);
        Double profit = Math.max(travelFee - refuelingCost, 0.0);

        log.debug("Calculated profit for vehicle ID {}: {} EUR", vehicle.getId(), profit);
        return profit;
    }

    private Double calculateTravelFee(Integer passengers, Double distance) {
        double timeInMinutes = calculateTravelTime(distance);
        double halfHourPeriods = Math.ceil(timeInMinutes / 30.0);
        double travelFee = passengers * ((distance * 2) + (halfHourPeriods * 2));

        log.debug("Calculated travel fee for {} passengers and {} km: {} EUR", passengers, distance, travelFee);
        return travelFee;
    }

    private Double calculateRefuelingCost(VehicleResponseModel vehicle, Double distance) {
        Double refuelingCost = switch (vehicle.getFuelType()) {
            case FuelTypeModel.GASOLINE -> distance * 2;
            case FuelTypeModel.ELECTRIC -> distance;
            case FuelTypeModel.HYBRID -> calculateHybridFuelConsumption(distance);
        };

        log.debug("Calculated refueling cost for vehicle ID {}: {} EUR", vehicle.getId(), refuelingCost);
        return refuelingCost;
    }

    private Double calculateTravelTime(Double distance) {
        Double travelTime = (distance <= 50) ? distance * 2 : distance;

        log.debug("Calculated travel time for {} km: {} minutes", distance, travelTime);
        return travelTime;
    }

    private Double calculateHybridFuelConsumption(Double distance) {
        Double consumption = (distance <= 50) ? distance / 2 : (50 / 2d) + (distance - 50);

        log.debug("Calculated hybrid fuel consumption for {} km: {} km equivalent", distance, consumption);
        return consumption;
    }

    private boolean isVehicleEligibleForTrip(VehicleResponseModel vehicle, int passengers, double distance) {
        boolean eligible = (vehicle.getPassengerCapacity() - DRIVER) >= passengers &&
                vehicle.getRangeKm() >= distance &&
                isVehicleAvailable(vehicle);

        log.debug("Checked eligibility for vehicle ID {}: {}", vehicle.getId(), eligible);
        return eligible;
    }

    private boolean isVehicleAvailable(VehicleResponseModel vehicle) {
        boolean available = vehicle.getAvailableType() == AvailableTypeModel.AVAILABLE;
        log.debug("Checked availability for vehicle ID {}: {}", vehicle.getId(), available);
        return available;
    }
}
