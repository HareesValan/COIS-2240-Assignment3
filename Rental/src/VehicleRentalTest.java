import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VehicleRentalTest {

	// Part 2 Question 1
	@Test
    void testLicensePlate() {
        // VALID plates
        Vehicle v1 = new Car("Toyota", "Corolla", 2019, 4);
        v1.setLicensePlate("AAA100");
        assertEquals("AAA100", v1.getLicensePlate());

        Vehicle v2 = new Car("Honda", "Civic", 2020, 4);
        v2.setLicensePlate("ABC567");
        assertEquals("ABC567", v2.getLicensePlate());

        Vehicle v3 = new Car("Ford", "Focus", 2021, 4);
        v3.setLicensePlate("ZZZ999");
        assertEquals("ZZZ999", v3.getLicensePlate());


        // INVALID plates (should throw exception)

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
    }

}
