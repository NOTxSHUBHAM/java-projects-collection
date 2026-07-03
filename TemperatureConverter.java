import java.util.Scanner;

public class TemperatureConverter {

    public static double celsiusToFahrenheit(double c) { return (c * 9.0 / 5) + 32; }
    public static double fahrenheitToCelsius(double f) { return (f - 32) * 5.0 / 9; }
    public static double celsiusToKelvin(double c)     { return c + 273.15; }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== Temperature Converter =====");
            System.out.println("1. Celsius    -> Fahrenheit");
            System.out.println("2. Fahrenheit -> Celsius");
            System.out.println("3. Celsius    -> Kelvin");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            if (choice == 4) { running = false; break; }

            System.out.print("Enter temperature value: ");
            double temp = sc.nextDouble();

            switch (choice) {
                case 1: System.out.printf("%.2f C = %.2f F%n", temp, celsiusToFahrenheit(temp)); break;
                case 2: System.out.printf("%.2f F = %.2f C%n", temp, fahrenheitToCelsius(temp)); break;
                case 3: System.out.printf("%.2f C = %.2f K%n", temp, celsiusToKelvin(temp));     break;
                default: System.out.println("Invalid choice.");
            }
        }
        System.out.println("Goodbye!");
        sc.close();
    }
}
