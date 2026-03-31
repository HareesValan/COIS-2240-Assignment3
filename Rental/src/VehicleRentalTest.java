import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;


class VehicleRentalTest {

	// Part 2 Question 1
	@Test
    void testLicensePlate() {
        // valid
        Vehicle v1 = new Car("Toyota", "Corolla", 2019, 4);
        v1.setLicensePlate("AAA100");
        assertEquals("AAA100", v1.getLicensePlate());

        Vehicle v2 = new Car("Honda", "Civic", 2020, 4);
        v2.setLicensePlate("ABC567");
        assertEquals("ABC567", v2.getLicensePlate());

        Vehicle v3 = new Car("Ford", "Focus", 2021, 4);
        v3.setLicensePlate("ZZZ999");
        assertEquals("ZZZ999", v3.getLicensePlate());


        // invalid
        Vehicle v4 = new Car("Test", "Car", 2020, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            v4.setLicensePlate("");
        });

        Vehicle v5 = new Car("Test", "Car", 2020, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            v5.setLicensePlate(null);
        });

        Vehicle v6 = new Car("Test", "Car", 2020, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            v6.setLicensePlate("AAA1000");
        });

        Vehicle v7 = new Car("Test", "Car", 2020, 5);
        assertThrows(IllegalArgumentException.class, () -> {
            v7.setLicensePlate("ZZZ99");
        });
    } // testLicensePlate
	
	// Part 2 Question 2
	@Test
	void testRentAndReturnVehicle() {
	    // object declaration
	    Vehicle v = new Car("Toyota", "Corolla", 2019, 4);
	    v.setLicensePlate("AAA111");
	    Customer c = new Customer(1, "George");

	    assertEquals(Vehicle.VehicleStatus.Available, v.getStatus()); // initial
	    RentalSystem system = RentalSystem.getInstance();

	    // rent
	    boolean rentSuccess = system.rentVehicle(v, c, java.time.LocalDate.now(), 100);
	    assertTrue(rentSuccess);
	    assertEquals(Vehicle.VehicleStatus.Rented, v.getStatus());
	    boolean rentFail = system.rentVehicle(v, c, java.time.LocalDate.now(), 100);
	    assertFalse(rentFail);

	    // return
	    boolean returnSuccess = system.returnVehicle(v, c, java.time.LocalDate.now(), 0);
	    assertTrue(returnSuccess);
	    assertEquals(Vehicle.VehicleStatus.Available, v.getStatus());
	    boolean returnFail = system.returnVehicle(v, c, java.time.LocalDate.now(), 0);
	    assertFalse(returnFail);
	} // testRentAndReturnVehicle

	// Part 2 Question 3
    @Test
    void testSingletonRentalSystem() throws Exception {
        // constrcutor of RentalSystem
        Constructor<RentalSystem> constructor = RentalSystem.class.getDeclaredConstructor();
        // check to ensure private
        int modifiers = constructor.getModifiers();
        assertTrue(Modifier.isPrivate(modifiers), "RentalSystem constructor is not private");

        // Check that getInstance returns a non-null instance
        RentalSystem instance = RentalSystem.getInstance();
        assertNotNull(instance, "getInstance() doesn't return a valid RentalSystem instance");
    }
}
