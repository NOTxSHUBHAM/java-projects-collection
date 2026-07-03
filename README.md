# Java Programming Projects — Complete Solutions
## Fr. C. Rodrigues College of Engineering

---

## How to Compile & Run Any Program

```bash
# Step 1: Navigate to module folder
cd module1   # (or module2, module3 etc.)

# Step 2: Compile
javac ProgramName.java

# Step 3: Run
java ProgramName
```

---

## MODULE 1 — Java Basics

### 1. CoffeeShop.java
- Menu-driven coffee ordering system
- Calculates subtotal → discount (if order ≥ ₹500) → 8% tax → total
- Prints itemized receipt
- **Concepts**: arrays, loops, Scanner, printf formatting

### 2. TemperatureConverter.java
- Converts Celsius ↔ Fahrenheit ↔ Kelvin
- Loop-based menu; user can convert multiple values
- **Concepts**: methods, switch-case, Scanner

### 3. ParkingFeeCalculator.java
- Calculates parking fee by vehicle type (Bike/Car/Truck) and hours
- First hour flat rate, 20% discount on subsequent hours
- **Concepts**: methods, conditional logic, Scanner

---

## MODULE 2 — OOP Concepts

### 4. BankingApplication.java
- Full banking system with deposit, withdraw, balance check
- Validates amounts (no negative deposits/withdrawals)
- **Concepts**: Class, Encapsulation, private fields, getters/setters

### 5. StudentManagementSystem.java
- Add / Delete / Update / Display student records
- Shows grade (A+, A, B, C, D, F) based on marks
- Uses ArrayList for dynamic storage
- **Concepts**: Classes, ArrayList, methods, OOP encapsulation

---

## MODULE 3 — Inheritance

### 6. ShapeDrawingApp.java
- Shapes: Circle, Rectangle, Triangle extend abstract Shape
- Supports resize() and rotate() operations
- Canvas class implements Drawable interface
- **Concepts**: Abstract class, Inheritance, Interface, Polymorphism, super keyword

### 7. EmployeePayroll.java
- FullTimeEmployee and PartTimeEmployee extend abstract Employee
- Calculates Gross Salary, Tax (slab-based), PF deductions, Net Salary
- Generates formatted Pay Stubs
- **Concepts**: Abstract class, method overriding, ArrayList, inheritance

---

## MODULE 4 — Arrays & Vectors

### 8. LibraryManagementApp.java
- Stores books in array; search by title or author keyword
- Checkout and return book functionality
- Pre-loaded with 4 sample books
- **Concepts**: Arrays, String methods (contains, toLowerCase)

### 9. ContactManagementApp.java
- Uses Java Vector<Contact> for dynamic storage
- Add, Search, Sort (alphabetically), Delete, Export (CSV)
- **Concepts**: Vector, Lambda, Comparator, ArrayList methods

---

## MODULE 5 — Strings

### 10. StringEncoding.java
- Run-Length Encoding & Decoding  (e.g., "aaabbb" → "a3b3")
- Caesar Cipher encrypt/decrypt (with custom shift)
- Word frequency analysis
- String statistics (vowels, consonants, digits, palindrome check)
- **Concepts**: String manipulation, StringBuilder, charAt, regex, streams

### 11. WordFrequencyNLP.java
- Word cloud generator with stop-word filtering
- NLP: Named Entity Recognition (Persons, Dates, Orgs, Emails via Regex)
- Sentence/word/char count
- Multi-line text input
- **Concepts**: Regex (Pattern/Matcher), LinkedHashMap, Streams, split/join

---

## MODULE 6 — Exception Handling

### 12. FlightBookingSystem.java
**Custom Exceptions**:
- `InvalidInputException` — bad passenger name
- `FlightNotFoundException` — wrong flight number
- `NoSeatsAvailableException` — flight is full
- `PaymentFailedException` — insufficient balance

Uses try-catch-finally on every booking attempt
- **Concepts**: Custom exceptions, try-catch-throw-finally, ArrayList

### 13. TransportationManagement.java
**Custom Exceptions**:
- `VehicleBreakdownException`
- `RouteNotFoundException`
- `DeliveryDelayException`
- `TrafficCongestionException`

**Resilience Patterns**:
- Circuit Breaker (halts system after 3 consecutive failures)
- Retry mechanism (reroutes on traffic congestion)
- **Concepts**: Exception chaining, custom exceptions, resilience patterns

---

## MODULE 7 — Multithreading

### 14. ChatApplication.java
- 3 users (Alice, Bob, Charlie) each run on separate threads
- Shared ChatRoom uses ReentrantLock for thread-safe message storage
- Background NotificationService (daemon thread) detects new messages
- Uses ExecutorService (Thread Pool) with 3 threads
- **Concepts**: Runnable, Thread, ExecutorService, ReentrantLock, daemon threads, synchronized access

### 15. SocialMediaPlatform.java
- User profiles, friends, posts, likes, notifications
- Thread-safe using ConcurrentHashMap, CopyOnWriteArrayList, BlockingQueue
- Posts dispatched via thread pool; likes processed asynchronously
- AtomicInteger for thread-safe like counting
- **Concepts**: ConcurrentHashMap, CopyOnWriteArrayList, AtomicInteger, BlockingQueue, ExecutorService

---

## MODULE 10 — File Handling

### 16. FileManagerApp.java
- Full file system manager: Create, Read, Write, Delete, Rename, Copy
- Lists directory contents with type and size
- Shows file metadata (creation time, modified time, permissions)
- Error handling for all IO operations
- **Concepts**: File, FileReader, FileWriter, BufferedReader/Writer, Files (NIO), BasicFileAttributes

### 17. WeatherForecastingApp.java
- Loads weather CSV data from file (BufferedReader)
- Saves data back to file (BufferedWriter)
- Object Serialization (ObjectOutputStream) and Deserialization (ObjectInputStream)
- RandomAccessFile to append records directly without rewriting entire file
- Statistics: avg/min/max temp, humidity grouped by city
- **Concepts**: BufferedReader/Writer, Serialization, RandomAccessFile, Streams, Optional

---

## Key Java Concepts Summary

| Module | Core Concepts Used |
|--------|-------------------|
| 1      | Variables, Scanner, Arrays, Methods, Switch |
| 2      | Classes, Objects, Encapsulation, ArrayList |
| 3      | Abstract Class, Interface, Inheritance, Polymorphism |
| 4      | Arrays, Vector, Sorting, Lambda |
| 5      | String methods, StringBuilder, Regex, Streams |
| 6      | Custom Exceptions, try-catch-throw-finally |
| 7      | Threads, Runnable, Lock, ExecutorService, Concurrent Collections |
| 10     | File I/O, Streams, Serialization, RandomAccessFile |
