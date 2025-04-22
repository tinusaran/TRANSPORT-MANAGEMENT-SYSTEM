package dao;

import entity.Vehicle;
import entity.Routes;
import entity.Trip;
import entity.Bookings;
import entity.Passengers;
import entity.Driver;
import exception.BookingNotFoundException;
import exception.VehicleNotFoundException;

import java.util.List;

public interface TransportManagementService {

    // Vehicle Management
    boolean addVehicle(Vehicle vehicle);
    boolean updateVehicle(Vehicle vehicle);
    boolean deleteVehicle(int vehicleId);

    Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException;

    // Trip Management
    boolean scheduleTrip(int vehicleId, int routeId, String departureDate, String arrivalDate, String tripType, int maxPassengers);

    boolean cancelTrip(int tripId);

    // Booking Management
    boolean bookTrip(int tripId, int passengerId, String bookingDate);
    boolean cancelBooking(int bookingId);

    // Driver Allocation
    boolean allocateDriver(int tripId, int driverId);
    boolean deallocateDriver(int tripId);

    // Booking Retrieval
    List<Bookings> getBookingsByPassenger(int passengerId) throws BookingNotFoundException;
    List<Bookings> getBookingsByTrip(int tripId);

    // Driver Retrieval
    List<Driver> getAvailableDrivers();
}
