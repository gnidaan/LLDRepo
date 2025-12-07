package entity;

import vehicle.Vehicle;

import java.util.Date;
import java.util.UUID;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private long exitTimestamp;
    private final long entryTimestamp;


    public ParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.spot = parkingSpot;
        this.entryTimestamp = new Date().getTime();
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public long getExitTimestamp() {
        return exitTimestamp;
    }

    public void setExitTimestamp() {
        this.exitTimestamp = new Date().getTime();
    }

    public long getEntryTimestamp() {
        return entryTimestamp;
    }
}
