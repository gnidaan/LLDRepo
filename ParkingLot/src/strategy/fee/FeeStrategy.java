package strategy.fee;

import entity.ParkingTicket;

public interface FeeStrategy {

    public double calculateFee(ParkingTicket ticket);


}
