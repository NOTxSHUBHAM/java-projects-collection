import java.util.Scanner;

public class ParkingFeeCalculator {

    // Returns fee based on vehicle type and hours
    public static double calculateFee(String vehicleType, double hours) {
        double ratePerHour;
        switch (vehicleType.toLowerCase()) {
            case "bike":  ratePerHour = 10; break;
            case "car":   ratePerHour = 30; break;
            case "truck": ratePerHour = 60; break;
            default:
                System.out.println("Unknown vehicle type. Using default rate.");
                ratePerHour = 20;
        }
        if (hours <= 1) return ratePerHour;           // flat first-hour charge
        return ratePerHour * (1 + (hours - 1) * 0.8); // 20% discount on subsequent hours
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Parking Fee Calculator =====");
        System.out.println("Supported vehicle types: bike | car | truck");
        System.out.print("Enter vehicle type: ");
        String vehicle = sc.next();
        System.out.print("Enter parking duration (hours): ");
        double hours = sc.nextDouble();

        if (hours <= 0) {
            System.out.println("Invalid duration.");
        } else {
            double fee = calculateFee(vehicle, hours);
            System.out.println("\n------- Parking Receipt -------");
            System.out.printf("Vehicle  : %s%n", vehicle.toUpperCase());
            System.out.printf("Duration : %.1f hour(s)%n", hours);
            System.out.printf("Total Fee: Rs. %.2f%n", fee);
        }
        sc.close();
    }
}
