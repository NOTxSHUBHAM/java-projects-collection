import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

// ─── Custom Exceptions ───
class VehicleBreakdownException  extends Exception { public VehicleBreakdownException(String msg)  { super(msg); } }
class RouteNotFoundException     extends Exception { public RouteNotFoundException(String msg)     { super(msg); } }
class DeliveryDelayException     extends Exception { public DeliveryDelayException(String msg)     { super(msg); } }
class TrafficCongestionException extends Exception { public TrafficCongestionException(String msg) { super(msg); } }

// ─── Vehicle ───
class Vehicle {
    String id, type;
    boolean isOperational;
    int health; // 0-100

    public Vehicle(String id, String type) {
        this.id   = id; this.type = type;
        this.isOperational = true;
        this.health = 80 + new Random().nextInt(20);
    }

    public void checkHealth() throws VehicleBreakdownException {
        if (health < 20) {
            isOperational = false;
            throw new VehicleBreakdownException("Vehicle " + id + " has broken down! Health: " + health + "%");
        }
        if (!isOperational)
            throw new VehicleBreakdownException("Vehicle " + id + " is already out of service.");
    }

    public void degrade() { health = Math.max(0, health - new Random().nextInt(30)); }

    @Override
    public String toString() {
        return String.format("Vehicle: %-8s | Type: %-10s | Health: %d%% | Status: %s",
                id, type, health, isOperational ? "Operational" : "Out of Service");
    }
}

// ─── Route ───
class Route {
    String routeId, from, to;
    double distanceKm;
    boolean hasTraffic;

    public Route(String id, String from, String to, double dist) {
        this.routeId = id; this.from = from; this.to = to;
        this.distanceKm = dist;
        this.hasTraffic = new Random().nextBoolean();
    }

    public double estimatedTime(double speed) throws TrafficCongestionException {
        double time = distanceKm / speed;
        if (hasTraffic) {
            time *= 1.5;
            throw new TrafficCongestionException(
                "Traffic on " + from + " -> " + to + " route! ETA increased to " + String.format("%.1f", time) + " hrs");
        }
        return time;
    }

    @Override
    public String toString() {
        return String.format("Route: %-6s | %-12s -> %-12s | %.0f km | Traffic: %s",
                routeId, from, to, distanceKm, hasTraffic ? "YES" : "No");
    }
}

// ─── Delivery ───
class Delivery {
    String deliveryId, cargo;
    String vehicleId, routeId;
    String status;

    public Delivery(String id, String cargo, String vehicleId, String routeId) {
        this.deliveryId = id; this.cargo = cargo;
        this.vehicleId  = vehicleId; this.routeId = routeId;
        this.status     = "Pending";
    }

    @Override
    public String toString() {
        return String.format("Delivery: %-8s | Cargo: %-15s | Vehicle: %-6s | Route: %-6s | Status: %s",
                deliveryId, cargo, vehicleId, routeId, status);
    }
}

// ─── Transportation System ───
public class TransportationManagement {
    static ArrayList<Vehicle>  vehicles  = new ArrayList<>();
    static ArrayList<Route>    routes    = new ArrayList<>();
    static ArrayList<Delivery> deliveries = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // Circuit Breaker (simple)
    static int failureCount = 0;
    static final int CIRCUIT_THRESHOLD = 3;

    static void circuitBreakerCheck() throws Exception {
        if (failureCount >= CIRCUIT_THRESHOLD)
            throw new Exception("CIRCUIT BREAKER OPEN: Too many failures (" + failureCount + "). System paused.");
    }

    static void dispatchDelivery() {
        try {
            circuitBreakerCheck();
            System.out.println("\nAvailable Vehicles:"); vehicles.forEach(System.out::println);
            System.out.println("\nAvailable Routes:"); routes.forEach(System.out::println);

            sc.nextLine();
            System.out.print("Enter Vehicle ID: "); String vId = sc.nextLine();
            System.out.print("Enter Route ID: ");   String rId = sc.nextLine();
            System.out.print("Enter Cargo desc: "); String cargo = sc.nextLine();

            Vehicle v = vehicles.stream().filter(x -> x.id.equalsIgnoreCase(vId)).findFirst()
                    .orElseThrow(() -> new RouteNotFoundException("Vehicle not found: " + vId));
            Route r = routes.stream().filter(x -> x.routeId.equalsIgnoreCase(rId)).findFirst()
                    .orElseThrow(() -> new RouteNotFoundException("Route not found: " + rId));

            v.degrade();
            v.checkHealth();

            double eta;
            try {
                eta = r.estimatedTime(60); // 60 km/h speed
                System.out.printf("ETA: %.1f hrs (no traffic)%n", eta);
            } catch (TrafficCongestionException e) {
                System.out.println("[Warning] " + e.getMessage());
                // Retry with reroute
                System.out.println("[Retry] Attempting alternate route at reduced speed...");
                eta = r.distanceKm / 40.0;
                System.out.printf("New ETA: %.1f hrs%n", eta);
            }

            if (eta > 5) throw new DeliveryDelayException("Delivery will be delayed by more than 5 hours!");

            String delId = "DEL" + (deliveries.size() + 1);
            Delivery d = new Delivery(delId, cargo, v.id, r.routeId);
            d.status = "In Transit";
            deliveries.add(d);
            System.out.println("Dispatched: " + d);
            failureCount = 0; // reset on success

        } catch (RouteNotFoundException | VehicleBreakdownException | DeliveryDelayException e) {
            failureCount++;
            System.out.println("[Error] " + e.getMessage() + " (Failures: " + failureCount + ")");
        } catch (Exception e) {
            System.out.println("[System] " + e.getMessage());
        } finally {
            System.out.println("[Log] Dispatch attempt recorded.");
        }
    }

    static void viewStatus() {
        System.out.println("\n===== Delivery Status =====");
        if (deliveries.isEmpty()) System.out.println("No deliveries.");
        else deliveries.forEach(System.out::println);
    }

    static void loadData() {
        vehicles.add(new Vehicle("V001", "Truck"));
        vehicles.add(new Vehicle("V002", "Van"));
        vehicles.add(new Vehicle("V003", "Bike"));
        routes.add(new Route("R01", "Mumbai",    "Pune",     150));
        routes.add(new Route("R02", "Delhi",     "Agra",     200));
        routes.add(new Route("R03", "Bangalore", "Chennai",  350));
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("===== Transportation Management System =====");
        boolean running = true;
        while (running) {
            System.out.println("\n1. View Vehicles & Routes  2. Dispatch Delivery  3. View Status  4. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();
            switch (ch) {
                case 1:
                    System.out.println("\n-- Vehicles --"); vehicles.forEach(System.out::println);
                    System.out.println("-- Routes --");   routes.forEach(System.out::println); break;
                case 2: dispatchDelivery(); break;
                case 3: viewStatus();       break;
                case 4: running = false;    break;
                default: System.out.println("Invalid.");
            }
        }
        sc.close();
    }
}
