package strategy.parking;

import entity.ParkingFloor;
import entity.ParkingSpot;
import vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

public interface ParkingStrategy {

    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle);;

}
