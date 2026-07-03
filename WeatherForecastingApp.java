import java.io.*;
import java.util.*;

// ─── WeatherData (Serializable for Object Persistence) ───
class WeatherData implements Serializable {
    private static final long serialVersionUID = 1L;
    String date, city, condition;
    double tempMin, tempMax, humidity, windSpeed;

    public WeatherData(String date, String city, String cond, double min, double max, double hum, double wind) {
        this.date      = date;   this.city      = city;
        this.condition = cond;   this.tempMin   = min;
        this.tempMax   = max;    this.humidity  = hum;
        this.windSpeed = wind;
    }

    public double getAvgTemp() { return (tempMin + tempMax) / 2; }

    @Override
    public String toString() {
        return String.format("%-12s | %-15s | %-12s | Temp: %.1f-%.1f C | Humidity: %.0f%% | Wind: %.1f km/h",
                date, city, condition, tempMin, tempMax, humidity, windSpeed);
    }
}

// ─── Weather Forecasting App ───
public class WeatherForecastingApp {
    static final String DATA_FILE  = "weather_data.txt";
    static final String OBJ_FILE   = "weather_objects.dat";
    static List<WeatherData> weatherList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    // ─── Load from text file (CSV-like) ───
    static void loadFromFile() {
        weatherList.clear();
        File f = new File(DATA_FILE);
        if (!f.exists()) { System.out.println("No data file found. Loading defaults."); loadDefaults(); return; }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    weatherList.add(new WeatherData(
                        parts[0].trim(), parts[1].trim(), parts[2].trim(),
                        Double.parseDouble(parts[3].trim()),
                        Double.parseDouble(parts[4].trim()),
                        Double.parseDouble(parts[5].trim()),
                        Double.parseDouble(parts[6].trim())
                    ));
                }
            }
            System.out.println("Loaded " + weatherList.size() + " records from file.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
            loadDefaults();
        }
    }

    // ─── Save to text file ───
    static void saveToFile() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            bw.write("Date,City,Condition,TempMin,TempMax,Humidity,WindSpeed\n");
            for (WeatherData w : weatherList)
                bw.write(String.format("%s,%s,%s,%.1f,%.1f,%.1f,%.1f%n",
                        w.date, w.city, w.condition, w.tempMin, w.tempMax, w.humidity, w.windSpeed));
        }
        System.out.println("Data saved to " + DATA_FILE);
    }

    // ─── Serialization ───
    static void serializeData() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OBJ_FILE))) {
            oos.writeObject(weatherList);
        }
        System.out.println("Data serialized to " + OBJ_FILE);
    }

    @SuppressWarnings("unchecked")
    static void deserializeData() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OBJ_FILE))) {
            weatherList = (List<WeatherData>) ois.readObject();
        }
        System.out.println("Deserialized " + weatherList.size() + " records.");
    }

    // ─── Display current weather ───
    static void displayCurrent() {
        System.out.print("Enter city name: "); String city = sc.nextLine();
        Optional<WeatherData> latest = weatherList.stream()
                .filter(w -> w.city.equalsIgnoreCase(city))
                .reduce((a, b) -> b); // get last (most recent)
        if (latest.isPresent()) {
            System.out.println("\n===== Current Weather =====");
            System.out.println(latest.get());
            String icon = getWeatherIcon(latest.get().condition);
            System.out.println("Condition: " + icon + " " + latest.get().condition);
        } else System.out.println("No data found for city: " + city);
    }

    static String getWeatherIcon(String condition) {
        switch (condition.toLowerCase()) {
            case "sunny":  return "[SUN]";
            case "rainy":  return "[RAIN]";
            case "cloudy": return "[CLOUD]";
            case "windy":  return "[WIND]";
            case "stormy": return "[STORM]";
            default:       return "[?]";
        }
    }

    // ─── Statistics ───
    static void showStats() {
        if (weatherList.isEmpty()) { System.out.println("No data."); return; }
        System.out.println("\n===== Weather Statistics =====");

        // Group by city
        Map<String, List<WeatherData>> byCity = new LinkedHashMap<>();
        for (WeatherData w : weatherList)
            byCity.computeIfAbsent(w.city, k -> new ArrayList<>()).add(w);

        for (Map.Entry<String, List<WeatherData>> e : byCity.entrySet()) {
            List<WeatherData> data = e.getValue();
            double avgTemp = data.stream().mapToDouble(WeatherData::getAvgTemp).average().orElse(0);
            double maxTemp = data.stream().mapToDouble(d -> d.tempMax).max().orElse(0);
            double minTemp = data.stream().mapToDouble(d -> d.tempMin).min().orElse(0);
            double avgHum  = data.stream().mapToDouble(d -> d.humidity).average().orElse(0);
            System.out.printf("%-15s | Avg Temp: %.1f C | Max: %.1f C | Min: %.1f C | Avg Humidity: %.0f%%%n",
                    e.getKey(), avgTemp, maxTemp, minTemp, avgHum);
        }
    }

    // ─── Random Access File: append single record fast ───
    static void appendRecord() {
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE, "rw")) {
            raf.seek(raf.length()); // go to end
            sc.nextLine();
            System.out.print("Date (YYYY-MM-DD): "); String date  = sc.nextLine();
            System.out.print("City: ");               String city  = sc.nextLine();
            System.out.print("Condition: ");          String cond  = sc.nextLine();
            System.out.print("Min Temp: ");           double tMin  = sc.nextDouble();
            System.out.print("Max Temp: ");           double tMax  = sc.nextDouble();
            System.out.print("Humidity: ");           double hum   = sc.nextDouble();
            System.out.print("Wind Speed: ");         double wind  = sc.nextDouble();
            raf.writeBytes(String.format("%s,%s,%s,%.1f,%.1f,%.1f,%.1f%n", date, city, cond, tMin, tMax, hum, wind));
            System.out.println("Record appended to file.");
            loadFromFile(); // reload
        } catch (IOException e) { System.out.println("Error: " + e.getMessage()); }
    }

    // ─── Defaults ───
    static void loadDefaults() {
        weatherList.add(new WeatherData("2024-01-01", "Mumbai",    "Sunny",  22, 34, 75, 12));
        weatherList.add(new WeatherData("2024-01-02", "Mumbai",    "Rainy",  20, 28, 90, 20));
        weatherList.add(new WeatherData("2024-01-01", "Delhi",     "Cloudy", 8,  18, 55, 15));
        weatherList.add(new WeatherData("2024-01-02", "Delhi",     "Windy",  6,  16, 50, 35));
        weatherList.add(new WeatherData("2024-01-01", "Bangalore", "Sunny",  18, 27, 60, 10));
    }

    public static void main(String[] args) {
        System.out.println("===== Weather Forecasting Application =====");
        loadFromFile();
        boolean running = true;
        while (running) {
            System.out.println("\n1. View Current Weather  2. Show All Records  3. Statistics");
            System.out.println("4. Save to File          5. Serialize Data    6. Load (Deserialize)");
            System.out.println("7. Append Record         8. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();
            try {
                switch (ch) {
                    case 1: displayCurrent(); break;
                    case 2:
                        System.out.println("\n===== All Weather Records =====");
                        weatherList.forEach(System.out::println); break;
                    case 3: showStats();         break;
                    case 4: saveToFile();        break;
                    case 5: serializeData();     break;
                    case 6: deserializeData();   break;
                    case 7: appendRecord();      break;
                    case 8: running = false;     break;
                    default: System.out.println("Invalid.");
                }
            } catch (Exception e) { System.out.println("[Error] " + e.getMessage()); }
        }
        sc.close();
    }
}
