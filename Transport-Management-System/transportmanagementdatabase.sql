use transportmanagement;
create table if not exists Vehicles(
    VehicleID int primary key auto_increment,
    Model varchar(255) not null,
    Capacity decimal(10,2) not null,
    Type varchar(50) not null,
    Status varchar(50) not null
    );
create table if not exists Routes(
	RouteID int primary key auto_increment,
    StartDestination varchar(255) not null,
    EndDestination varchar(255) not null,
    Distance decimal(10,2) not null
);
CREATE TABLE Trips (
    TripID INT AUTO_INCREMENT PRIMARY KEY,
    VehicleID INT,
    RouteID INT,
    DepartureDate DATETIME NOT NULL,
    ArrivalDate DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,
    TripType VARCHAR(50) DEFAULT 'Freight',
    MaxPassengers INT,
    FOREIGN KEY (VehicleID) REFERENCES Vehicles(VehicleID) ON DELETE CASCADE,
    FOREIGN KEY (RouteID) REFERENCES Routes(RouteID) ON DELETE CASCADE
);
CREATE TABLE Passengers (
    PassengerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(255) NOT NULL,
    Gender VARCHAR(255) NOT NULL,
    Age INT NOT NULL,
    Email VARCHAR(255) UNIQUE NOT NULL,
    PhoneNumber VARCHAR(50) NOT NULL
);
CREATE TABLE Bookings (
    BookingID INT AUTO_INCREMENT PRIMARY KEY,
    TripID INT,
    PassengerID INT,
    BookingDate DATETIME NOT NULL,
    Status VARCHAR(50) NOT NULL,
    FOREIGN KEY (TripID) REFERENCES Trips(TripID) ON DELETE CASCADE,
    FOREIGN KEY (PassengerID) REFERENCES Passengers(PassengerID) ON DELETE CASCADE
);
show tables;

INSERT INTO Vehicles (Model, Capacity, Type, Status) VALUES
('Volvo XC90', 5, 'Bus', 'Available'),
('Ford Transit', 12, 'Van', 'On Trip'),
('Mercedes Actros', 20, 'Truck', 'Maintenance'),
('Tata Ace', 10, 'Van', 'Available'),
('Ashok Leyland', 25, 'Bus', 'Available'),
('Mahindra Blazo', 30, 'Truck', 'On Trip'),
('Scania Touring', 50, 'Bus', 'Available'),
('Isuzu NPR', 15, 'Truck', 'On Trip'),
('Force Traveller', 20, 'Van', 'Available'),
('Eicher Pro 3016', 16, 'Truck', 'Maintenance');

INSERT INTO Routes (StartDestination, EndDestination, Distance) VALUES
('Chennai', 'Bangalore', 350.00),
('Mumbai', 'Pune', 150.00),
('Delhi', 'Agra', 200.00),
('Hyderabad', 'Vijayawada', 270.00),
('Kolkata', 'Durgapur', 180.00),
('Ahmedabad', 'Surat', 250.00),
('Jaipur', 'Jodhpur', 340.00),
('Lucknow', 'Kanpur', 90.00),
('Patna', 'Ranchi', 320.00),
('Coimbatore', 'Madurai', 200.00);

INSERT INTO Trips (VehicleID, RouteID, DepartureDate, ArrivalDate, Status, TripType, MaxPassengers) VALUES
(1, 1, '2025-03-18 09:00:00', '2025-03-18 15:00:00', 'Scheduled', 'Passenger', 50),
(2, 2, '2025-03-19 08:00:00', '2025-03-19 12:00:00', 'In Progress', 'Freight', NULL),
(3, 3, '2025-03-20 07:30:00', '2025-03-20 11:30:00', 'Completed', 'Freight', NULL),
(4, 4, '2025-03-21 06:45:00', '2025-03-21 10:45:00', 'Scheduled', 'Passenger', 20),
(5, 5, '2025-03-22 10:00:00', '2025-03-22 14:00:00', 'Cancelled', 'Passenger', 25),
(6, 6, '2025-03-23 09:15:00', '2025-03-23 13:15:00', 'Scheduled', 'Freight', NULL),
(7, 7, '2025-03-24 08:45:00', '2025-03-24 12:45:00', 'Completed', 'Passenger', 50),
(8, 8, '2025-03-25 07:30:00', '2025-03-25 11:30:00', 'Scheduled', 'Freight', NULL),
(9, 9, '2025-03-26 06:00:00', '2025-03-26 10:00:00', 'Cancelled', 'Passenger', 30),
(10, 10, '2025-03-27 11:00:00', '2025-03-27 15:00:00', 'Scheduled', 'Passenger', 40);


INSERT INTO Passengers (FirstName, Gender, Age, Email, PhoneNumber) VALUES
('Rajesh', 'Male', 35, 'rajesh@gmail.com', '9876543210'),
('Priya', 'Female', 28, 'priya@gmail.com', '9876543211'),
('Arun', 'Male', 42, 'arun@gmail.com', '9876543212'),
('Sneha', 'Female', 30, 'sneha@gmail.com', '9876543213'),
('Vikas', 'Male', 40, 'vikas@gmail.com', '9876543214'),
('Meera', 'Female', 27, 'meera@gmail.com', '9876543215'),
('Ravi', 'Male', 38, 'ravi@gmail.com', '9876543216'),
('Pooja', 'Female', 26, 'pooja@gmail.com', '9876543217'),
('Sandeep', 'Male', 45, 'sandeep@gmail.com', '9876543218'),
('Anjali', 'Female', 33, 'anjali@gmail.com', '9876543219');


INSERT INTO Bookings (TripID, PassengerID, BookingDate, Status) VALUES
(1, 1, '2025-03-10 14:30:00', 'Confirmed'),
(2, 2, '2025-03-11 15:00:00', 'Cancelled'),
(3, 3, '2025-03-12 16:15:00', 'Completed'),
(4, 4, '2025-03-13 12:45:00', 'Confirmed'),
(5, 5, '2025-03-14 09:30:00', 'Cancelled'),
(6, 6, '2025-03-15 11:20:00', 'Confirmed'),
(7, 7, '2025-03-16 14:00:00', 'Completed'),
(8, 8, '2025-03-17 18:10:00', 'Confirmed'),
(9, 9, '2025-03-18 19:00:00', 'Cancelled'),
(10, 10, '2025-03-19 07:50:00', 'Confirmed');

select * from vehicles;
select * from trips;
select * from passengers;
select * from bookings;


CREATE TABLE Drivers (
    DriverID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    LicenseNumber VARCHAR(50) NOT NULL,
    Contact VARCHAR(15) NOT NULL,
    Status VARCHAR(20) DEFAULT 'Active'  -- Status can be 'Active' or 'Inactive'
);

INSERT INTO Drivers (Name, LicenseNumber, Contact, Status)
VALUES 
('John Doe', 'ABC123456', '9876543210', 'Active'),
('Jane Smith', 'XYZ987654', '9123456789', 'Active'),
('Michael Johnson', 'LMN456789', '9234567890', 'Inactive'),
('Emily Davis', 'DEF112233', '9345678901', 'Active'),
('David Wilson', 'GHI223344', '9456789012', 'Active'),
('Robert Brown', 'JKL334455', '9567890123', 'Active'),
('Sarah Miller', 'MNO445566', '9678901234', 'Inactive'),
('William Anderson', 'PQR556677', '9789012345', 'Active'),
('Linda Thomas', 'STU667788', '9890123456', 'Active'),
('James Moore', 'VWX778899', '9901234567', 'Inactive'),
('Patricia White', 'YZA889900', '9012345678', 'Active'),
('Richard Taylor', 'BCD990011', '9123456780', 'Active');


select * from Vehicles;
select * from routes;
select * from trips;
select * from passengers;
select * from bookings;
select * from drivers;

ALTER TABLE Trips ADD COLUMN DriverID INT;
ALTER TABLE Trips 
ADD CONSTRAINT fk_driver FOREIGN KEY (DriverID) REFERENCES Drivers(DriverID) ON DELETE SET NULL;









