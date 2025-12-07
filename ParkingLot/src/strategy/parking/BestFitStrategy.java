package strategy.parking;

import entity.ParkingFloor;
import entity.ParkingSpot;
import vehicle.Vehicle;

import java.util.List;
import java.util.Optional;

public class BestFitStrategy implements ParkingStrategy{
    @Override
    public Optional<ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        Optional<ParkingSpot> bestSpot = Optional.empty();

        for (ParkingFloor floor: floors){
            Optional<ParkingSpot> availableSpot = floor.findAvailableSpot(vehicle);
            if (availableSpot.isPresent()){
                if (bestSpot.isEmpty()){
                    bestSpot = availableSpot;
                }
                else {
                    if (availableSpot.get().getSpotSize().ordinal() < bestSpot.get().getSpotSize().ordinal()){
                        bestSpot = availableSpot;
                    }
                }

            }


        }
        return bestSpot;

    }
}
