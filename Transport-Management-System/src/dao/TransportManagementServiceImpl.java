package dao;
import java.sql.*;
import java.util.*;

import entity.Bookings;
import entity.Vehicle;
import entity.Trip;
import entity.Passengers;
import entity.Routes;
import entity.Driver;
import exception.BookingNotFoundException;
import util.DBConnection;

import exception.*;


public class TransportManagementServiceImpl implements TransportManagementService{
    private Connection connection;

    public TransportManagementServiceImpl(){
        this.connection = DBConnection.getConnection();
    }
    public boolean addVehicle(Vehicle vehicle){
        String query = ("INSERT INTO Vehicles (Model, Capacity, Type, Status) VALUES (?, ?, ?, ?)");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1,vehicle.getModel());
            preparedStatement.setDouble(2,vehicle.getCapacity());
            preparedStatement.setString(3,vehicle.getType());
            preparedStatement.setString(4,vehicle.getStatus());

            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted>0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateVehicle(Vehicle vehicle){
        String query = ("UPDATE Vehicles SET Model = ?, Capacity = ?, Type = ?, Status = ? WHERE VehicleID = ?");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,vehicle.getModel());
            preparedStatement.setDouble(2,vehicle.getCapacity());
            preparedStatement.setString(3,vehicle.getType());
            preparedStatement.setString(4,vehicle.getStatus());
            preparedStatement.setInt(5,vehicle.getVehicleId());

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated>0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteVehicle(int vehicleId) {
        String query = "DELETE FROM Vehicles WHERE VehicleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, vehicleId);

            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Vehicle getVehicleById(int vehicleId) throws VehicleNotFoundException {
        String sql = "SELECT * FROM Vehicles WHERE VehicleID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // return vehicle object
                return new Vehicle(
                        rs.getInt("VehicleID"),
                        rs.getString("Model"),
                        rs.getInt("Capacity"),
                        rs.getString("Type"),
                        rs.getString("Status")
                );
            } else {
                throw new VehicleNotFoundException("Vehicle with ID " + vehicleId + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean scheduleTrip(int vehicleId, int routeId, String departureDate, String arrivalDate, String tripType, int maxPassengers) {
        String sql = "INSERT INTO Trips (VehicleID, RouteID, DepartureDate, ArrivalDate, TripType, MaxPassengers, Status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, vehicleId);
            preparedStatement.setInt(2, routeId);
            preparedStatement.setString(3, departureDate);
            preparedStatement.setString(4, arrivalDate);
            preparedStatement.setString(5, tripType);
            preparedStatement.setInt(6, maxPassengers);
            //preparedStatement.setString(7, status);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean cancelTrip(int tripId) {
        String sql = "UPDATE Trips SET Status = 'Cancelled' WHERE TripID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tripId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean bookTrip(int tripId, int passengerId, String bookingDate) {
        String sql = "INSERT INTO Bookings (TripID, PassengerID, BookingDate, Status) VALUES (?, ?, ?, 'Confirmed')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tripId);
            preparedStatement.setInt(2, passengerId);
            preparedStatement.setString(3, bookingDate);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE Bookings SET Status = 'Cancelled' WHERE BookingID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, bookingId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean allocateDriver(int tripId, int driverId) {
        String sql = "UPDATE Trips SET DriverID = ? WHERE TripID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, driverId);
            preparedStatement.setInt(2, tripId);

            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deallocateDriver(int tripId) {
        return false;
    }

    // Retrieve all bookings for a given passenger
    public List<Bookings> getBookingsByPassenger(int passengerId)  throws BookingNotFoundException {
        String sql = "SELECT * FROM Bookings WHERE PassengerID = ?";
        List<Bookings> bookings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, passengerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookings.add(new Bookings(
                        resultSet.getInt("BookingID"),
                        resultSet.getInt("TripID"),
                        resultSet.getInt("PassengerID"),
                        resultSet.getTimestamp("BookingDate").toLocalDateTime(),
                        resultSet.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings found for passenger ID " + passengerId);
        }
        return bookings;
    }

    // Retrieve all bookings for a specific trip
    public List<Bookings> getBookingsByTrip(int tripId) {
        String sql = "SELECT * FROM Bookings WHERE TripID = ?";
        List<Bookings> bookings = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, tripId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bookings.add(new Bookings(
                        resultSet.getInt("BookingID"),
                        resultSet.getInt("TripID"),
                        resultSet.getInt("PassengerID"),
                        resultSet.getTimestamp("BookingDate").toLocalDateTime(),
                        resultSet.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Retrieve all available drivers who are not assigned to any scheduled trip
    public List<Driver> getAvailableDrivers() {
        String sql = "SELECT * FROM Drivers WHERE DriverID NOT IN " +
                "(SELECT DISTINCT DriverID FROM Trips WHERE Status = 'Scheduled' AND DriverID IS NOT NULL)";
        List<Driver> drivers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                drivers.add(new Driver(
                        resultSet.getInt("DriverID"),
                        resultSet.getString("Name"),
                        resultSet.getString("LicenseNumber"),
                        resultSet.getString("Contact")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

}
