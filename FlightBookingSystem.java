import java.util.Scanner;
import java.util.ArrayList;

// ─── Custom Exceptions ───
class InvalidInputException    extends Exception { public InvalidInputException(String msg)    { super(msg); } }
class NoSeatsAvailableException extends Exception { public NoSeatsAvailableException(String msg) { super(msg); } }
class PaymentFailedException   extends Exception { public PaymentFailedException(String msg)   { super(msg); } }
class FlightNotFoundException  extends Exception { public FlightNotFoundException(String msg)  { super(msg); } }

// ─── Flight class ───
class Flight {
    String flightNo, source, destination;
    int totalSeats, availableSeats;
    double price;

    public Flight(String flightNo, String source, String dest, int seats, double price) {
        this.flightNo       = flightNo;
        this.source         = source;
        this.destination    = dest;
        this.totalSeats     = seats;
        this.availableSeats = seats;
        this.price          = price;
    }

    public void bookSeat() throws NoSeatsAvailableException {
        if (availableSeats <= 0)
            throw new NoSeatsAvailableException("No seats available on flight " + flightNo);
        availableSeats--;
    }

    @Override
    public String toString() {
        return String.format("Flight: %-8s | %s -> %-15s | Seats: %d | Price: Rs.%.0f",
                flightNo, source, destination, availableSeats, price);
    }
}

// ─── Booking class ───
class Booking {
    static int idCounter = 1000;
    int bookingId;
    String passengerName, flightNo;
    double amountPaid;

    public Booking(String name, String flightNo, double amount) {
        this.bookingId     = ++idCounter;
        this.passengerName = name;
        this.flightNo      = flightNo;
        this.amountPaid    = amount;
    }

    @Override
    public String toString() {
        return String.format("Booking ID: %d | Passenger: %-20s | Flight: %s | Paid: Rs.%.2f",
                bookingId, passengerName, flightNo, amountPaid);
    }
}

// ─── Main System ───
public class FlightBookingSystem {
    static ArrayList<Flight>  flights  = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static void validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().length() < 2)
            throw new InvalidInputException("Passenger name must be at least 2 characters.");
    }

    static void processPayment(double amount, double balance) throws PaymentFailedException {
        if (balance < amount)
            throw new PaymentFailedException("Insufficient wallet balance. Required: Rs." + amount + ", Available: Rs." + balance);
    }

    static Flight findFlight(String flightNo) throws FlightNotFoundException {
        for (Flight f : flights)
            if (f.flightNo.equalsIgnoreCase(flightNo)) return f;
        throw new FlightNotFoundException("Flight " + flightNo + " not found.");
    }

    static void viewFlights() {
        System.out.println("\n===== Available Flights =====");
        for (Flight f : flights) System.out.println(f);
    }

    static void bookFlight() {
        try {
            sc.nextLine();
            System.out.print("Passenger Name: "); String name = sc.nextLine();
            validateName(name);

            viewFlights();
            System.out.print("Enter Flight No: "); String fNo = sc.nextLine();
            Flight flight = findFlight(fNo);

            flight.bookSeat();

            System.out.printf("Total amount: Rs.%.2f%n", flight.price);
            System.out.print("Enter wallet balance: Rs."); double balance = sc.nextDouble();
            processPayment(flight.price, balance);

            Booking b = new Booking(name, flight.flightNo, flight.price);
            bookings.add(b);
            System.out.println("\n✓ Booking Confirmed!");
            System.out.println(b);

        } catch (InvalidInputException e) {
            System.out.println("[Input Error] " + e.getMessage());
        } catch (FlightNotFoundException e) {
            System.out.println("[Not Found] " + e.getMessage());
        } catch (NoSeatsAvailableException e) {
            System.out.println("[Seat Error] " + e.getMessage());
        } catch (PaymentFailedException e) {
            System.out.println("[Payment Error] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[Unexpected Error] " + e.getMessage());
        } finally {
            System.out.println("[System] Booking process completed.");
        }
    }

    static void viewBookings() {
        if (bookings.isEmpty()) { System.out.println("No bookings yet."); return; }
        System.out.println("\n===== All Bookings =====");
        for (Booking b : bookings) System.out.println(b);
    }

    static void loadFlights() {
        flights.add(new Flight("AI101", "Mumbai",  "Delhi",     120, 4500));
        flights.add(new Flight("SG202", "Delhi",   "Bangalore",  80, 3800));
        flights.add(new Flight("6E303", "Mumbai",  "Chennai",    60, 3200));
        flights.add(new Flight("UK404", "Kolkata", "Mumbai",      2, 5100)); // limited seats
    }

    public static void main(String[] args) {
        loadFlights();
        System.out.println("===== Flight Booking System =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. View Flights  2. Book Flight  3. View Bookings  4. Exit");
            System.out.print("Choice: ");
            try {
                switch (sc.nextInt()) {
                    case 1: viewFlights();  break;
                    case 2: bookFlight();   break;
                    case 3: viewBookings(); break;
                    case 4: running = false; break;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Input error: " + e.getMessage());
                sc.nextLine();
            }
        }
        sc.close();
    }
}
