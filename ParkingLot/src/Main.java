import entity.ParkingFloor;
import entity.ParkingSpot;
import entity.ParkingTicket;
import strategy.fee.VehicleBasedFeeStrategy;
import strategy.parking.BestFitStrategy;
import vehicle.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    IO.println(String.format("Hello and welcome!"));

    ParkingLot parkingLot=ParkingLot.getInstance();

    ParkingFloor parkingFloor1=new ParkingFloor(1);
    parkingFloor1.addSpot(new ParkingSpot("F1-S1", VehicleSize.SMALL));
    parkingFloor1.addSpot(new ParkingSpot("F1-M1", VehicleSize.MEDIUM));
    parkingFloor1.addSpot(new ParkingSpot("F1-L1", VehicleSize.LARGE));

    ParkingFloor parkingFloor2=new ParkingFloor(2);
    parkingFloor2.addSpot(new ParkingSpot("F2-S1", VehicleSize.SMALL));
    parkingFloor2.addSpot(new ParkingSpot("F2-M1", VehicleSize.MEDIUM));
    parkingFloor2.addSpot(new ParkingSpot("F2-L1", VehicleSize.LARGE));

    parkingLot.addFloor(parkingFloor1);
    parkingLot.addFloor(parkingFloor2);

    Vehicle bike = new Bike("B-123");
    Vehicle car = new Car("C-456");
    Vehicle truck = new Truck("T-789");

    System.out.println("\n--- Availability before parking ---");
    parkingLot.displayAvailability();

    parkingLot.setFeeStrategy(new VehicleBasedFeeStrategy());

    Optional<ParkingTicket> bikeTicketOpt = parkingLot.parkVehicle(bike);

    Optional<ParkingTicket> carTicketOpt = parkingLot.parkVehicle(car);

    Optional<ParkingTicket> truckTicketOpt = parkingLot.parkVehicle(truck);

    System.out.println("\n--- Availability after parking ---");
    parkingLot.displayAvailability();
    if (carTicketOpt.isPresent()) {
        Optional<Double> feeOpt = parkingLot.unParkVehicle(car.getLicenseNumber());
        feeOpt.ifPresent(fee -> System.out.printf("Car C-456 unparked. Fee: $%.2f\n", fee));
    }

    System.out.println("\n--- Availability after one car leaves ---");
    parkingLot.displayAvailability();



}
