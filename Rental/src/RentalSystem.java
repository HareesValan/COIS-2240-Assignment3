import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RentalSystem {
	// initialize instance
	private static RentalSystem instance;
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private RentalHistory rentalHistory = new RentalHistory();
    
    // Part 1 Question 3 / call in constructor
    private RentalSystem() {
        loadData(); // call to loadData
    }
    
    // get instance method / Part 1 Question 1
    public static RentalSystem getInstance() {
        if (instance == null) {
            instance = new RentalSystem();
        }
        return instance;
    }

    public boolean addVehicle(Vehicle vehicle) {
    	// Part 1 Question 4
        if (findVehicleByPlate(vehicle.getLicensePlate()) != null) {
            System.out.println("A vehicle with the license plate " + vehicle.getLicensePlate() + " exists.");
            return false;
        }

        vehicles.add(vehicle);
        saveVehicle(vehicle); // Part 1 Question 2
        return true;
    }

    public boolean addCustomer(Customer customer) {
    	// Part 1 Question 4
    	if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("A customer with the ID " + customer.getCustomerId() + " exists.");
            return false;
        }
    	
        customers.add(customer);
        saveCustomer(customer); // Part 1 Question 2
        return true;
    }

    public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Available) {
            vehicle.setStatus(Vehicle.VehicleStatus.Rented);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, amount, "RENT"));
            saveRecord(rentalHistory.getRentalHistory().get(rentalHistory.getRentalHistory().size()-1)); // Part 1 Question 2
            update(); // Part 1 Question 2 / update vehicles.txt
            System.out.println("Vehicle rented to " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not available for renting.");
        }
    }

    public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.Rented) {
            vehicle.setStatus(Vehicle.VehicleStatus.Available);
            rentalHistory.addRecord(new RentalRecord(vehicle, customer, date, extraFees, "RETURN"));
            saveRecord(rentalHistory.getRentalHistory().get(rentalHistory.getRentalHistory().size()-1)); // Part 1 Question 2
            update(); // Part 1 Question 2 / update vehicles.txt
            System.out.println("Vehicle returned by " + customer.getCustomerName());
        }
        else {
            System.out.println("Vehicle is not rented.");
        }
    }    

    public void displayVehicles(Vehicle.VehicleStatus status) {
        // Display appropriate title based on status
        if (status == null) {
            System.out.println("\n=== All Vehicles ===");
        } else {
            System.out.println("\n=== " + status + " Vehicles ===");
        }
        
        // Header with proper column widths
        System.out.printf("|%-16s | %-12s | %-12s | %-12s | %-6s | %-18s |%n", 
            " Type", "Plate", "Make", "Model", "Year", "Status");
        System.out.println("|--------------------------------------------------------------------------------------------|");
    	  
        boolean found = false;
        for (Vehicle vehicle : vehicles) {
            if (status == null || vehicle.getStatus() == status) {
                found = true;
                String vehicleType;
                if (vehicle instanceof Car) {
                    vehicleType = "Car";
                } else if (vehicle instanceof Minibus) {
                    vehicleType = "Minibus";
                } else if (vehicle instanceof PickupTruck) {
                    vehicleType = "Pickup Truck";
                } else {
                    vehicleType = "Unknown";
                }
                System.out.printf("| %-15s | %-12s | %-12s | %-12s | %-6d | %-18s |%n", 
                    vehicleType, vehicle.getLicensePlate(), vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getStatus().toString());
            }
        }
        if (!found) {
            if (status == null) {
                System.out.println("  No Vehicles found.");
            } else {
                System.out.println("  No vehicles with Status: " + status);
            }
        }
        System.out.println();
    }

    public void displayAllCustomers() {
        for (Customer c : customers) {
            System.out.println("  " + c.toString());
        }
    }
    
    public void displayRentalHistory() {
        if (rentalHistory.getRentalHistory().isEmpty()) {
            System.out.println("  No rental history found.");
        } else {
            // Header with proper column widths
            System.out.printf("|%-10s | %-12s | %-20s | %-12s | %-12s |%n", 
                " Type", "Plate", "Customer", "Date", "Amount");
            System.out.println("|-------------------------------------------------------------------------------|");
            
            for (RentalRecord record : rentalHistory.getRentalHistory()) {                
                System.out.printf("| %-9s | %-12s | %-20s | %-12s | $%-11.2f |%n", 
                    record.getRecordType(), 
                    record.getVehicle().getLicensePlate(),
                    record.getCustomer().getCustomerName(),
                    record.getRecordDate().toString(),
                    record.getTotalAmount()
                );
            }
            System.out.println();
        }
    }
    
    public Vehicle findVehicleByPlate(String plate) {
        for (Vehicle v : vehicles) {
            if (v.getLicensePlate().equalsIgnoreCase(plate)) {
                return v;
            }
        }
        return null;
    }
    
    public Customer findCustomerById(int id) {
        for (Customer c : customers)
            if (c.getCustomerId() == id)
                return c;
        return null;
    }
    
    // Part 1 Question 2 / save a single vehicle by appending it into vehicles.txt
    private void saveVehicle(Vehicle vehicle) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", true))) {
            writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getStatus());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } // saveVehicle
    
    // Part 1 Question 2 / save a single customer by appending it into customers.txt
    private void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customer.getCustomerId() + "," + customer.getCustomerName());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } // saveCustomer
    
    // Part 1 Question 2 / save a rental record by appending it into rental_records.txt
    private void saveRecord(RentalRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rental_records.txt", true))) {
            writer.write(record.getRecordType() + "," + record.getVehicle().getLicensePlate() + "," + record.getCustomer().getCustomerId() + "," + record.getRecordDate() + "," + record.getTotalAmount());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } // saveRecord
    
    // update vehicle.txt for renting and returning
    private void update() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("vehicles.txt", false))) {
            for (Vehicle v : vehicles) {
                writer.write(v.getLicensePlate() + "," + v.getMake() + "," + v.getModel() + "," + v.getYear() + "," + v.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    } // update
    
    // Part 1 Question 3
    private void loadData() {
        // load vehicles
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("vehicles.txt"))) {
            while (sc.hasNextLine()) {
                String[] parts1 = sc.nextLine().split(",");
                Vehicle v = new Car(parts1[1], parts1[2], Integer.parseInt(parts1[3]), 4); // load everything as a car / temporary
                v.setLicensePlate(parts1[0]);
                v.setStatus(Vehicle.VehicleStatus.valueOf(parts1[4]));
                vehicles.add(v);
            }
        } catch (java.io.FileNotFoundException e) { }

        // load the customers
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("customers.txt"))) {
            while (sc.hasNextLine()) {
                String[] parts2 = sc.nextLine().split(",");
                customers.add(new Customer(Integer.parseInt(parts2[0]), parts2[1]));
            }
        } catch (java.io.FileNotFoundException e) { }

        // load the rental records
        try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("rental_records.txt"))) {
            while (sc.hasNextLine()) {
                String[] parts3 = sc.nextLine().split(",");
                Vehicle v = findVehicleByPlate(parts3[1]);
                Customer c = findCustomerById(Integer.parseInt(parts3[2]));
                if (v != null && c != null) {
                    rentalHistory.addRecord(new RentalRecord(v, c, java.time.LocalDate.parse(parts3[3]), Double.parseDouble(parts3[4]), parts3[0]));
                }
            }
        } catch (java.io.FileNotFoundException e) { }
    } // loadData
    
}