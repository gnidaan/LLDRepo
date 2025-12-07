package entity;

import vehicle.Vehicle;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingFloor {

    private final int floorNumber;

    private final Map<String,ParkingSpot> parkingSpot;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.parkingSpot = new ConcurrentHashMap<>();
    }

    public void addSpot(ParkingSpot parkingSpot){
        this.parkingSpot.put(parkingSpot.getSpotId(),parkingSpot);
    }

    public synchronized Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle) {

    return this.parkingSpot.values().stream().
            filter(spot -> !spot.isOccupied() && spot.canFitVehicle(vehicle))
            .sorted(Comparator.comparing(ParkingSpot::getSpotSize))
            .findFirst();
    }

    public Map<String, ParkingSpot> getParkingSpot() {
        return parkingSpot;
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}
