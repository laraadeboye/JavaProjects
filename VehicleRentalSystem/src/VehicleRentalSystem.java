/**
 * Vehicle Rental System
 * A comprehensive car rental agency application that manages different vehicle types
 * using interfaces to enforce common behavior and ensure consistency.
 */

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Vehicle Interface
 * Defines the contract for all vehicle types with common properties.
 * All vehicles must implement methods to retrieve make, model, and year.
 */
interface Vehicle {
    String getMake();
    String getModel();
    int getYear();
}

/**
 * CarVehicle Interface
 * Extends vehicle behavior with car-specific properties.
 * Defines methods for managing door count and fuel type.
 */
interface CarVehicle {
    void setNumberOfDoors(int doors);
    int getNumberOfDoors();
    void setFuelType(String fuelType);
    String getFuelType();
}

/**
 * MotorVehicle Interface
 * Extends vehicle behavior with motorcycle-specific properties.
 * Defines methods for managing wheel count and motorcycle type.
 */
interface MotorVehicle {
    void setNumberOfWheels(int wheels);
    int getNumberOfWheels();
    void setMotorcycleType(String type);
    String getMotorcycleType();
}

/**
 * TruckVehicle Interface
 * Extends vehicle behavior with truck-specific properties.
 * Defines methods for managing cargo capacity and transmission type.
 */
interface TruckVehicle {
    void setCargoCapacity(double capacity);
    double getCargoCapacity();
    void setTransmissionType(String transmission);
    String getTransmissionType();
}

/**
 * Car Class
 * Implements Vehicle and CarVehicle interfaces.
 * Represents a car with make, model, year, doors, and fuel type.
 */
class Car implements Vehicle, CarVehicle {
    private String make;
    private String model;
    private int year;
    private int numberOfDoors;
    private String fuelType;

    /**
     * Constructor for Car class
     * @param make The manufacturer of the car
     * @param model The model name of the car
     * @param year The year of manufacture
     */
    public Car(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setNumberOfDoors(int doors) {
        this.numberOfDoors = doors;
    }

    @Override
    public int getNumberOfDoors() {
        return numberOfDoors;
    }

    @Override
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Returns a formatted string representation of the car
     */
    @Override
    public String toString() {
        return String.format("Car Details:\n  Make: %s\n  Model: %s\n  Year: %d\n  Doors: %d\n  Fuel Type: %s",
                make, model, year, numberOfDoors, fuelType);
    }
}

/**
 * Motorcycle Class
 * Implements Vehicle and MotorVehicle interfaces.
 * Represents a motorcycle with make, model, year, wheels, and type.
 */
class Motorcycle implements Vehicle, MotorVehicle {
    private String make;
    private String model;
    private int year;
    private int numberOfWheels;
    private String motorcycleType;

    /**
     * Constructor for Motorcycle class
     * @param make The manufacturer of the motorcycle
     * @param model The model name of the motorcycle
     * @param year The year of manufacture
     */
    public Motorcycle(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setNumberOfWheels(int wheels) {
        this.numberOfWheels = wheels;
    }

    @Override
    public int getNumberOfWheels() {
        return numberOfWheels;
    }

    @Override
    public void setMotorcycleType(String type) {
        this.motorcycleType = type;
    }

    @Override
    public String getMotorcycleType() {
        return motorcycleType;
    }

    /**
     * Returns a formatted string representation of the motorcycle
     */
    @Override
    public String toString() {
        return String.format("Motorcycle Details:\n  Make: %s\n  Model: %s\n  Year: %d\n  Wheels: %d\n  Type: %s",
                make, model, year, numberOfWheels, motorcycleType);
    }
}

/**
 * Truck Class
 * Implements Vehicle and TruckVehicle interfaces.
 * Represents a truck with make, model, year, cargo capacity, and transmission.
 */
class Truck implements Vehicle, TruckVehicle {
    private String make;
    private String model;
    private int year;
    private double cargoCapacity;
    private String transmissionType;

    /**
     * Constructor for Truck class
     * @param make The manufacturer of the truck
     * @param model The model name of the truck
     * @param year The year of manufacture
     */
    public Truck(String make, String model, int year) {
        this.make = make;
        this.model = model;
        this.year = year;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setCargoCapacity(double capacity) {
        this.cargoCapacity = capacity;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public void setTransmissionType(String transmission) {
        this.transmissionType = transmission;
    }

    @Override
    public String getTransmissionType() {
        return transmissionType;
    }

    /**
     * Returns a formatted string representation of the truck
     */
    @Override
    public String toString() {
        return String.format("Truck Details:\n  Make: %s\n  Model: %s\n  Year: %d\n  Cargo Capacity: %.2f tons\n  Transmission: %s",
                make, model, year, cargoCapacity, transmissionType);
    }
}

/**
 * VehicleRentalSystem - Main Program
 * Interactive system for managing vehicle information in a car rental agency.
 * Allows users to create and display details of different vehicle types.
 */
public class VehicleRentalSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();

    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        System.out.println("=== Vehicle Rental Agency Information System ===\n");

        boolean continueAdding = true;

        while (continueAdding) {
            try {
                System.out.println("\nSelect Vehicle Type:");
                System.out.println("1. Car");
                System.out.println("2. Motorcycle");
                System.out.println("3. Truck");
                System.out.println("4. Display All Vehicles");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addCar();
                        break;
                    case 2:
                        addMotorcycle();
                        break;
                    case 3:
                        addTruck();
                        break;
                    case 4:
                        displayAllVehicles();
                        break;
                    case 5:
                        continueAdding = false;
                        System.out.println("\nThank you for using the Vehicle Rental System!");
                        break;
                    default:
                        System.out.println("Invalid choice! Please select 1-5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input! Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Adds a new car to the system with user-provided details
     */
    private static void addCar() {
        try {
            System.out.println("\n--- Adding a Car ---");

            System.out.print("Enter make: ");
            String make = scanner.nextLine();

            System.out.print("Enter model: ");
            String model = scanner.nextLine();

            System.out.print("Enter year of manufacture: ");
            int year = scanner.nextInt();
            scanner.nextLine();

            // Validate year
            if (year < 1886 || year > 2026) {
                throw new IllegalArgumentException("Invalid year! Must be between 1886 and 2026.");
            }

            Car car = new Car(make, model, year);

            System.out.print("Enter number of doors (2 or 4): ");
            int doors = scanner.nextInt();
            scanner.nextLine();

            if (doors != 2 && doors != 4) {
                throw new IllegalArgumentException("Invalid number of doors! Must be 2 or 4.");
            }
            car.setNumberOfDoors(doors);

            System.out.print("Enter fuel type (petrol/diesel/electric): ");
            String fuelType = scanner.nextLine().toLowerCase();

            if (!fuelType.equals("petrol") && !fuelType.equals("diesel") && !fuelType.equals("electric")) {
                throw new IllegalArgumentException("Invalid fuel type! Must be petrol, diesel, or electric.");
            }
            car.setFuelType(fuelType);

            vehicles.add(car);
            System.out.println("Car added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type!");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Adds a new motorcycle to the system with user-provided details
     */
    private static void addMotorcycle() {
        try {
            System.out.println("\n--- Adding a Motorcycle ---");

            System.out.print("Enter make: ");
            String make = scanner.nextLine();

            System.out.print("Enter model: ");
            String model = scanner.nextLine();

            System.out.print("Enter year of manufacture: ");
            int year = scanner.nextInt();
            scanner.nextLine();

            if (year < 1886 || year > 2026) {
                throw new IllegalArgumentException("Invalid year! Must be between 1886 and 2026.");
            }

            Motorcycle motorcycle = new Motorcycle(make, model, year);

            System.out.print("Enter number of wheels (2 or 3): ");
            int wheels = scanner.nextInt();
            scanner.nextLine();

            if (wheels < 2 || wheels > 3) {
                throw new IllegalArgumentException("Invalid number of wheels! Must be 2 or 3.");
            }
            motorcycle.setNumberOfWheels(wheels);

            System.out.print("Enter motorcycle type (sport/cruiser/off-road): ");
            String type = scanner.nextLine().toLowerCase();

            if (!type.equals("sport") && !type.equals("cruiser") && !type.equals("off-road")) {
                throw new IllegalArgumentException("Invalid type! Must be sport, cruiser, or off-road.");
            }
            motorcycle.setMotorcycleType(type);

            vehicles.add(motorcycle);
            System.out.println("Motorcycle added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type!");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Adds a new truck to the system with user-provided details
     */
    private static void addTruck() {
        try {
            System.out.println("\n--- Adding a Truck ---");

            System.out.print("Enter make: ");
            String make = scanner.nextLine();

            System.out.print("Enter model: ");
            String model = scanner.nextLine();

            System.out.print("Enter year of manufacture: ");
            int year = scanner.nextInt();
            scanner.nextLine();

            if (year < 1886 || year > 2026) {
                throw new IllegalArgumentException("Invalid year! Must be between 1886 and 2026.");
            }

            Truck truck = new Truck(make, model, year);

            System.out.print("Enter cargo capacity (in tons): ");
            double capacity = scanner.nextDouble();
            scanner.nextLine();

            if (capacity <= 0 || capacity > 100) {
                throw new IllegalArgumentException("Invalid capacity! Must be between 0 and 100 tons.");
            }
            truck.setCargoCapacity(capacity);

            System.out.print("Enter transmission type (manual/automatic): ");
            String transmission = scanner.nextLine().toLowerCase();

            if (!transmission.equals("manual") && !transmission.equals("automatic")) {
                throw new IllegalArgumentException("Invalid transmission! Must be manual or automatic.");
            }
            truck.setTransmissionType(transmission);

            vehicles.add(truck);
            System.out.println("Truck added successfully!");

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input type!");
            scanner.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Displays all vehicles currently in the system
     */
    private static void displayAllVehicles() {
        System.out.println("\n=== All Vehicles in System ===");

        if (vehicles.isEmpty()) {
            System.out.println("No vehicles have been added yet.");
            return;
        }

        for (int i = 0; i < vehicles.size(); i++) {
            System.out.println("\nVehicle " + (i + 1) + ":");
            System.out.println(vehicles.get(i).toString());
        }
    }
}