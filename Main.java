import java.util.Scanner;

class Vehicle {
    protected String vehicleId;
    protected String brand;
    protected String model;
    protected double basePricePerDay;
    protected boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }
}

class Car extends Vehicle {
    public Car(String vehicleId, String brand, String model, double basePricePerDay) {
        super(vehicleId, brand, model, basePricePerDay);
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private Car[] cars;
    private Customer[] customers;
    private Rental[] rentals;
    private int carCount;
    private int customerCount;
    private int rentalCount;

    public CarRentalSystem(int maxCars, int maxCustomers, int maxRentals) {
        cars = new Car[maxCars];
        customers = new Customer[maxCustomers];
        rentals = new Rental[maxRentals];
        carCount = 0;
        customerCount = 0;
        rentalCount = 0;
    }

    public void addCar(Car car) {
        if (carCount < cars.length) {
            cars[carCount] = car;
            carCount++;
        } else {
            System.out.println("Cannot add more cars. Array is full.");
        }
    }

    public void addCustomer(Customer customer) {
        if (customerCount < customers.length) {
            customers[customerCount] = customer;
            customerCount++;
        } else {
            System.out.println("Cannot add more customers. Array is full.");
        }
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            if (rentalCount < rentals.length) {
                rentals[rentalCount] = new Rental(car, customer, days);
                rentalCount++;
            } else {
                System.out.println("Cannot rent more cars. Array is full.");
            }
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnVehicle();
        for (int i = 0; i < rentalCount; i++) {
            if (rentals[i] != null && rentals[i].getCar() == car) {
                rentals[i] = null;
                break;
            }
        }
    }

public void menu() {
    Scanner scanner = new Scanner(System.in);
    char choose='y';
    while(choose=='y' || choose=='Y'){
        System.out.println("===== Car Rental System =====");
        System.out.println("1. Rent a Car");
        System.out.println("2. Return a Car");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice == 1) {
            if (carCount == 0) {
                System.out.println("No cars available for rent.");
            } else {
				System.out.println("\n----------------------------\n");
                System.out.println("Available Cars:");
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].isAvailable()) {
                        System.out.println(cars[i].getVehicleId() + " - " + cars[i].getBrand() + " " + cars[i].getModel());
                    }
                }
                System.out.print("Enter the car ID you want to rent: ");
                String carId = scanner.nextLine();
                Car selectedCar = null;
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].getVehicleId().equals(carId) && cars[i].isAvailable()) {
                        selectedCar = cars[i];
                        break;
                    }
                }
                if (selectedCar != null) {
                    System.out.print("Enter your name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter the number of days for rental: ");
                    int rentalDays = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    Customer newCustomer = new Customer("CUS" + (customerCount + 1), customerName);
                    addCustomer(newCustomer);
                    double totalPrice = selectedCar.calculatePrice(rentalDays);
					
                    System.out.println("\n== Rental Information ==\n");
                    System.out.println("Customer ID: " + newCustomer.getCustomerId());
                    System.out.println("Customer Name: " + newCustomer.getName());
                    System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
                    System.out.println("Rental Days: " + rentalDays);
                    System.out.printf("Total Price: $%.2f%n", totalPrice);
                    System.out.print("\nConfirm rental (Y/N): ");
                    String confirm = scanner.nextLine();
					
                    if (confirm.equalsIgnoreCase("Y")) {
                        rentCar(selectedCar, newCustomer, rentalDays);
                    } else {
                        System.out.println("\nRental canceled.");
                    }
                } else {
                    System.out.println("\nInvalid car selection or car not available for rent.");
                }
            }
        } else if (choice == 2) {
            if (carCount == 0) {
                System.out.println("No cars available to return.");
            } else {
                System.out.print("Enter the car ID you want to return: ");
                String carId = scanner.nextLine();
                Car carToReturn = null;
                for (int i = 0; i < carCount; i++) {
                    if (cars[i].getVehicleId().equals(carId) && !cars[i].isAvailable()) {
                        carToReturn = cars[i];
                        break;
                    }
                }
                if (carToReturn != null) {
                    Customer customer = null;
                    for (int i = 0; i < rentalCount; i++) {
                        if (rentals[i] != null && rentals[i].getCar() == carToReturn) {
                            customer = rentals[i].getCustomer();
                            returnCar(carToReturn);
                            System.out.println("Car returned successfully by " + customer.getName());
                            break;
                        }
                    }
                    if (customer == null) {
                        System.out.println("Car was not rented or rental information is missing.");
                    }
                } else {
                    System.out.println("Invalid car ID or car is not rented.");
                }
            }
        } else if (choice == 3) {
            System.out.println("\nThank you for using the Car Rental System!");
            break;
        } else {
            System.out.println("Invalid choice. Please enter a valid option.");
        }
		System.out.print("\nDo you want another service? ");
        choose=scanner.next().charAt(0);
    }
}


}
public class Main{
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem(10,10,10);

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0); // Different base price per day for each car
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Suzuki", "Swift", 150.0);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
	
}