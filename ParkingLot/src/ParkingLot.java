import entity.ParkingFloor;
import entity.ParkingSpot;
import entity.ParkingTicket;
import strategy.fee.FeeStrategy;
import strategy.parking.BestFitStrategy;
import strategy.parking.ParkingStrategy;
import vehicle.Vehicle;
import vehicle.VehicleSize;

import javax.xml.crypto.XMLCryptoContext;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParkingLot {

    private static ParkingLot instance;
    private final List<ParkingFloor> parkingFloors =new ArrayList<>();
    private ParkingStrategy parkingStrategy;
    private FeeStrategy feeStrategy;
    private Map<String, ParkingTicket> activeTickets;

    public ParkingLot() {
        this.parkingStrategy = new BestFitStrategy();
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static synchronized ParkingLot getInstance() {
        if (null == instance) instance = new ParkingLot();
        return instance;
    }


    public List<ParkingFloor> getParkingFloors() {
        return parkingFloors;
    }


    public void addFloor(ParkingFloor parkingFloors) {
        this.parkingFloors.add(parkingFloors);
    }

    public ParkingStrategy getParkingStrategy() {
        return parkingStrategy;
    }

    public void setParkingStrategy(ParkingStrategy parkingStrategy) {
        this.parkingStrategy = parkingStrategy;
    }

    public FeeStrategy getFeeStrategy() {
        return feeStrategy;
    }

    public void setFeeStrategy(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public Optional<ParkingTicket> parkVehicle(Vehicle vehicle) {
        Optional<ParkingSpot> parkingSpot = this.parkingStrategy.findSpot(this.parkingFloors, vehicle);
        if (parkingSpot.isPresent()){
            ParkingSpot parkingSpot1 = parkingSpot.get();
            parkingSpot1.parkVehicle(vehicle);
            ParkingTicket parkingTicket=new ParkingTicket(vehicle,parkingSpot1);
            this.activeTickets.put(vehicle.getLicenseNumber(),parkingTicket);
            System.out.printf("%s parked at %s. Ticket: %s\n", vehicle.getLicenseNumber(), parkingSpot1.getSpotId(), parkingTicket.getTicketId());
            return Optional.of(parkingTicket);
        }
        System.out.println("No available spot for " + vehicle.getLicenseNumber());
        return Optional.empty();

    }

    public Optional<Double> unParkVehicle(String vehicleLicenseNumber) {
        ParkingTicket removedParkingTicket = this.activeTickets.remove(vehicleLicenseNumber);
        if (removedParkingTicket==null){
            System.out.println("Ticket not found");
            return Optional.empty();
        }

        removedParkingTicket.setExitTimestamp();
        removedParkingTicket.getSpot().unParkVehicle();

        Double parkingFee = feeStrategy.calculateFee(removedParkingTicket);

        return Optional.of(parkingFee);

    }

    /**
     * Iterates through all floors and displays the number of available parking spots
     * grouped by their size (VehicleSize) for each floor.
     */
    public void displayAvailability() {
        System.out.println("\n--- 🅿️ Parking Lot Availability Report ---");

        // 1. Calculate and display detailed availability per floor
        this.parkingFloors.stream()
                .sorted(Comparator.comparing(ParkingFloor::getFloorNumber)) // Sort floors numerically
                .forEach(floor -> {
                    System.out.printf("\n## ➡️ Floor %d Availability ##\n", floor.getFloorNumber());

                    // Group available spots on the current floor by their size and count them
                    Map<VehicleSize, Long> availableCounts = floor.getParkingSpot().values().stream()
                            .filter(spot -> !spot.isOccupied())
                            // *** CORRECTION APPLIED HERE ***
                            .collect(Collectors.groupingBy(ParkingSpot::getSpotSize, Collectors.counting()));

                    boolean hasAvailability = false;

                    // Iterate through all possible VehicleSize enums for a complete report
                    for (VehicleSize size : VehicleSize.values()) {
                        long count = availableCounts.getOrDefault(size, 0L);
                        System.out.printf("  * **%s** spots: %d available\n", size.toString(), count);
                        if (count > 0) hasAvailability = true;
                    }

                    if (!hasAvailability && !floor.getParkingSpot().isEmpty()) {
                        System.out.println("  ❌ This floor is currently **FULL**.");
                    } else if (floor.getParkingSpot().isEmpty()) {
                        System.out.println("  ℹ️ This floor has no parking spots configured.");
                    }
                });

        // 2. Calculate and display the overall summary
        System.out.println("\n--- 🌐 Overall Parking Lot Summary ---");

        Map<VehicleSize, Long> totalAvailableCounts = this.parkingFloors.stream()
                .flatMap(floor -> floor.getParkingSpot().values().stream()) // Combine all spots from all floors
                .filter(spot -> !spot.isOccupied()) // Filter only available spots
                // *** CORRECTION APPLIED HERE ***
                .collect(Collectors.groupingBy(ParkingSpot::getSpotSize, Collectors.counting()));

        boolean overallAvailable = false;

        // Iterate through all possible VehicleSize enums for a complete summary
        for (VehicleSize size : VehicleSize.values()) {
            long count = totalAvailableCounts.getOrDefault(size, 0L);
            System.out.printf("  ✅ Total **%s** Spots Available: %d\n", size.toString(), count);
            if (count > 0) overallAvailable = true;
        }

        if (!overallAvailable) {
            System.out.println("The entire parking lot is currently **FULL**.");
        }
        System.out.println("-----------------------------------------\n");
    }
}
