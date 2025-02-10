# OTP Mobil Kft. - Taxi Fleet Management Backend API

## 1. Project Overview

This project is a backend application that provides a REST API for managing vehicles and suggesting optimal vehicle assignments for trips in a taxi fleet. 

## 2. Technology Stack

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Springdoc OpenAPI**
- **JUnit for Testing**
- **PostgreSQL / H2**
- **Maven**

## 3. Installation & Setup

### Prerequisites
- Java 21
- Maven
- PostgreSQL

### Steps to Run
1. Clone the repository:
   ```sh
   git clone https://github.com/BerciBAA/Taxi-fleet-manager
   ```
2. Navigate to the project directory:
   ```sh
   cd taxi-fleet-manager
   ```
3. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/taxi_fleet
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
4. Run the application:
   ```sh
   mvn spring-boot:run
   ```
5. Open Swagger UI:
   ```
   http://localhost:8080/swagger-ui/index.html#/
   ```

## 4. API Endpoints

### Vehicles

#### Add a New Vehicle
- **POST** `/vehicles`

- **Allowed Fuel Types:**
  - `gasoline`
  - `hybrid`
  - `electric`

- **Allowed Availability Types:**
  - `available`
  - `unavailable`

- **Request Body:**
  ```json
  {
    "passengerCapacity": 4,
    "rangeKm": 500,
    "fuelType": "gasoline",
    "availableType": "available"
  }
  ```

- **Response:**
  ```json
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "passengerCapacity": 4,
    "rangeKm": 500,
    "fuelType": "gasoline",
    "availableType": "available"
  }
  ```

#### Get All Vehicles
- **GET** `/vehicles`
- **Response:**
  ```json
  [
    {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "passengerCapacity": 4,
      "rangeKm": 500,
      "fuelType": "gasoline",
      "availableType": "available"
    }
  ]
  ```

### Trip Suggestions

#### Get Suggested Vehicles for a Trip
- **GET** `/trips/suggestions`
- **Query Parameters:**
  ```
  passengers=3
  distance=100.0
  ```
- **Response:**
  ```json
  [
    {
      "vehicleId": "c00db520-cd2b-48b6-9a49-2f592d16a539",
      "passengerCapacity": 10,
      "rangeKm": 100.0,
      "fuelType": "hybrid",
      "profit": {
        "amount": 133.0,
        "currency": "EUR"
      }
    }
  ]
  ```

## 5. Testing

### Run Tests
```sh
mvn verify
```
