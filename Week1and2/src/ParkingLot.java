import java.util.*;

public class ParkingLot {

    class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            licensePlate = null;
            occupied = false;
        }
    }

    private ParkingSpot[] table;
    private int capacity = 500;
    private int occupiedCount = 0;
    private int totalProbes = 0;
    private int operations = 0;

    public ParkingLot() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % capacity; // linear probing
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;
        operations++;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (table[index].occupied) {

            if (plate.equals(table[index].licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);
                double fee = hours * 5; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                occupiedCount--;

                System.out.printf(
                        "Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        index, hours, fee
                );

                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found.");
    }

    // Find nearest available spot
    public int findNearestAvailableSpot() {

        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                return i;
            }
        }

        return -1;
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedCount * 100.0) / capacity;

        double avgProbes = operations == 0 ? 0 : (double) totalProbes / operations;

        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Avg Probes: %.2f\n", avgProbes);
    }

    // Test program
    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        System.out.println("Nearest Available Spot: " + lot.findNearestAvailableSpot());

        lot.getStatistics();
    }
}