import java.util.*;
import java.text.SimpleDateFormat;

// Room Class
class Room {
    private String roomType;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(String roomType, double pricePerNight) {
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Room Type: " + roomType + ", Price per Night: $" + pricePerNight + ", Available: " + isAvailable;
    }
}

// Hotel Class
class Hotel {
    private List<Room> rooms;

    public Hotel() {
        rooms = new ArrayList<>();
        rooms.add(new Room("Single", 100.0));
        rooms.add(new Room("Double", 150.0));
        rooms.add(new Room("Suite", 300.0));
        rooms.add(new Room("Double", 150.0));
    }

    public List<Room> searchAvailableRooms(String roomType) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomType().equalsIgnoreCase(roomType) && room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    public void displayRooms() {
        System.out.println("\nAvailable Rooms:");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}

// Reservation Class
class Reservation {
    private Room room;
    private String customerName;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Room room, String customerName, Date checkInDate, Date checkOutDate) {
        this.room = room;
        this.customerName = customerName;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        room.setAvailable(false); // Mark room as unavailable
    }

    public String getReservationDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "Reservation for " + customerName + "\nRoom Type: " + room.getRoomType() +
                "\nCheck-in Date: " + sdf.format(checkInDate) +
                "\nCheck-out Date: " + sdf.format(checkOutDate) +
                "\nTotal Cost: $" + calculateTotalCost();
    }

    public double calculateTotalCost() {
        long diff = checkOutDate.getTime() - checkInDate.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return days * room.getPricePerNight();
    }

    public String getCustomerName() {  // New method to access customerName
        return customerName;
    }
}

// Payment Processing Class
class PaymentProcessor {
    public static boolean processPayment(double amount, String paymentMethod) {
        System.out.println("\nProcessing payment of $" + amount + " via " + paymentMethod + "...");
        // In a real system, integrate with a payment gateway here
        return true;
    }
}

// Main Hotel Reservation System
public class HotelReservationSystem {
    private Hotel hotel;
    private List<Reservation> reservations;
    private Scanner scanner;

    public HotelReservationSystem() {
        hotel = new Hotel();
        reservations = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\nHotel Reservation System");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. View Booking Details");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    hotel.displayRooms();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    viewBookingDetails();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void makeReservation() {
        System.out.print("Enter room type (Single/Double/Suite): ");
        String roomType = scanner.nextLine();

        List<Room> availableRooms = hotel.searchAvailableRooms(roomType);
        if (availableRooms.isEmpty()) {
            System.out.println("No available rooms of type " + roomType);
            return;
        }

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        try {
            System.out.print("Enter check-in date (yyyy-MM-dd): ");
            Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

            System.out.print("Enter check-out date (yyyy-MM-dd): ");
            Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine());

            if (checkOutDate.before(checkInDate)) {
                System.out.println("Check-out date cannot be before check-in date.");
                return;
            }

            Room room = availableRooms.get(0); // Reserve the first available room
            Reservation reservation = new Reservation(room, customerName, checkInDate, checkOutDate);
            reservations.add(reservation);

            // Process Payment
            double amount = reservation.calculateTotalCost();
            System.out.print("Enter payment method (Credit Card/Debit Card): ");
            String paymentMethod = scanner.nextLine();
            boolean paymentStatus = PaymentProcessor.processPayment(amount, paymentMethod);

            if (paymentStatus) {
                System.out.println("Reservation confirmed!");
            } else {
                System.out.println("Payment failed. Reservation not confirmed.");
                room.setAvailable(true); // Revert room availability if payment fails
            }
        } catch (Exception e) {
            System.out.println("Error in reservation process. Please ensure the dates are in yyyy-MM-dd format.");
        }
    }

    private void viewBookingDetails() {
        System.out.print("Enter your name to view reservation details: ");
        String customerName = scanner.nextLine();

        boolean found = false;
        for (Reservation reservation : reservations) {
            if (reservation.getCustomerName().equalsIgnoreCase(customerName)) {
                System.out.println(reservation.getReservationDetails());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No reservation found for " + customerName);
        }
    }

    public static void main(String[] args) {
        HotelReservationSystem system = new HotelReservationSystem();
        system.start();
    }
}
