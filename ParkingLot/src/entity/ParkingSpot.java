package entity;

import vehicle.Vehicle;
import vehicle.VehicleSize;

public class ParkingSpot {

    private final String spotId;
    private Vehicle parkedVehicle;
    private boolean isOccupied;
    private final VehicleSize spotSize;


    public ParkingSpot(String spotId, VehicleSize spotSize) {
        this.spotId = spotId;
        this.spotSize = spotSize;
    }

    public String getSpotId() {
        return spotId;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public VehicleSize getSpotSize() {
        return spotSize;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        if (this.isOccupied)
            return false;

        switch (vehicle.getSize()){
            case SMALL -> {
                return this.spotSize == VehicleSize.SMALL;
            }
            case MEDIUM -> {
                return this.spotSize == VehicleSize.MEDIUM || this.spotSize == VehicleSize.LARGE;
            }
            case LARGE -> {
                return this.spotSize == VehicleSize.LARGE;
            }
            default -> {
                return false;
            }
        }

    }

    public void setParkedVehicle(Vehicle parkedVehicle) {
        this.parkedVehicle = parkedVehicle;
    }

    public synchronized void parkVehicle(Vehicle vehicle) {
        this.isOccupied = true;
        this.parkedVehicle =vehicle;
    }

    public synchronized void unParkVehicle() {
        this.isOccupied = false;
        this.parkedVehicle =null;
    }
}
