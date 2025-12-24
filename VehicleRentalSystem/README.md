
# Vehicle Rental System
A comprehensive Java console application for managing vehicle information in a car rental agency.

## Features

- Add new cars with doors and fuel type
- Add new motorcycles with wheels and type
- Add new trucks with cargo capacity and transmission
- View all vehicles in the system
- Input validation and error handling
- Interface-based design for extensibility

## Requirements

Java JDK 8 or higher

## How to Run

Compile the program:

```sh
javac VehicleRentalSystem.java
```

Run the program:

```sh
java VehicleRentalSystem
```

## Usage

The program displays a menu with 5 options:

- Add Car - Enter make, model, year, number of doors, and fuel type
- Add Motorcycle - Enter make, model, year, number of wheels, and motorcycle type
- Add Truck - Enter make, model, year, cargo capacity, and transmission type
- Display All Vehicles - Show all registered vehicles with their details
- Exit - Close the application

### Example
```
=== Vehicle Rental Agency Information System ===

Select Vehicle Type:
1. Car
2. Motorcycle
3. Truck
4. Display All Vehicles
5. Exit
Enter your choice: 1

--- Adding a Car ---
Enter make: Toyota
Enter model: Camry
Enter year of manufacture: 2023
Enter number of doors (2 or 4): 4
Enter fuel type (petrol/diesel/electric): petrol

Car added successfully!
```
## Project Structure
The application contains the following components:

#### Interfaces:

- Vehicle - Defines common methods for all vehicles (getMake, getModel, getYear)
- CarVehicle - Defines car-specific methods (doors and fuel type)
- MotorVehicle - Defines motorcycle-specific methods (wheels and type)
- TruckVehicle - Defines truck-specific methods (cargo capacity and transmission)

#### Classes:

- Car - Implements Vehicle and CarVehicle interfaces
- Motorcycle - Implements Vehicle and MotorVehicle interfaces
- Truck - Implements Vehicle and TruckVehicle interfaces
- VehicleRentalSystem - Main program with user interface and menu system

#### Validation Rules

- Year of manufacture must be between 1886 and 2026
- Number of doors must be 2 or 4
- Fuel type must be petrol, diesel, or electric
- Number of wheels must be 2 or 3
- Motorcycle type must be sport, cruiser, or off-road
- Cargo capacity must be between 0 and 100 tons
= Transmission type must be manual or automatic
- All required fields must be filled

## Technical Details

- Uses Java interfaces to enforce contracts across vehicle types
- Implements proper exception handling for invalid inputs
- Stores vehicles in an ArrayList for dynamic management
- Provides formatted output using toString() methods
- Follows object-oriented programming best practices
