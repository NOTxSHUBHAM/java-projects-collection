// ───── Module 3: Inheritance – Shape Drawing Application ─────

// Abstract base class
abstract class Shape {
    protected String color;
    protected double scaleFactor;
    protected double rotationAngle;

    public Shape(String color) {
        this.color         = color;
        this.scaleFactor   = 1.0;
        this.rotationAngle = 0.0;
    }

    public abstract double area();
    public abstract double perimeter();
    public abstract void draw();

    public void resize(double factor) {
        scaleFactor *= factor;
        System.out.printf("Shape resized by %.2fx (total scale: %.2fx)%n", factor, scaleFactor);
    }

    public void rotate(double angle) {
        rotationAngle = (rotationAngle + angle) % 360;
        System.out.printf("Shape rotated %.2f degrees (total: %.2f deg)%n", angle, rotationAngle);
    }

    public void info() {
        System.out.printf("Color: %-10s | Area: %8.2f | Perimeter: %8.2f | Scale: %.2f | Angle: %.1f%n",
                color, area() * scaleFactor * scaleFactor, perimeter() * scaleFactor, scaleFactor, rotationAngle);
    }
}

// ─── Circle ───
class Circle extends Shape {
    private double radius;
    public Circle(String color, double radius) { super(color); this.radius = radius; }

    @Override public double area()      { return Math.PI * radius * radius; }
    @Override public double perimeter() { return 2 * Math.PI * radius; }
    @Override public void draw() {
        System.out.println("Drawing Circle [color=" + color + ", radius=" + radius + "]");
    }
}

// ─── Rectangle ───
class Rectangle extends Shape {
    private double width, height;
    public Rectangle(String color, double w, double h) { super(color); width = w; height = h; }

    @Override public double area()      { return width * height; }
    @Override public double perimeter() { return 2 * (width + height); }
    @Override public void draw() {
        System.out.println("Drawing Rectangle [color=" + color + ", " + width + "x" + height + "]");
    }
}

// ─── Triangle ───
class Triangle extends Shape {
    private double a, b, c;
    public Triangle(String color, double a, double b, double c) {
        super(color); this.a = a; this.b = b; this.c = c;
    }

    @Override public double perimeter() { return a + b + c; }
    @Override public double area() {
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    @Override public void draw() {
        System.out.println("Drawing Triangle [color=" + color + ", sides=" + a + "," + b + "," + c + "]");
    }
}

// ─── Interface for Canvas ───
interface Drawable {
    void drawOnCanvas();
}

// ─── Canvas ───
class Canvas implements Drawable {
    private Shape[] shapes;
    private int count = 0;

    public Canvas(int capacity) { shapes = new Shape[capacity]; }

    public void addShape(Shape s) {
        if (count < shapes.length) shapes[count++] = s;
        else System.out.println("Canvas is full!");
    }

    @Override
    public void drawOnCanvas() {
        System.out.println("\n===== Drawing Canvas =====");
        for (int i = 0; i < count; i++) { System.out.print((i + 1) + ". "); shapes[i].draw(); shapes[i].info(); }
    }
}

// ─── Main ───
public class ShapeDrawingApp {
    public static void main(String[] args) {
        Canvas canvas = new Canvas(10);

        Circle    c = new Circle("Red", 5);
        Rectangle r = new Rectangle("Blue", 4, 6);
        Triangle  t = new Triangle("Green", 3, 4, 5);

        canvas.addShape(c);
        canvas.addShape(r);
        canvas.addShape(t);
        canvas.drawOnCanvas();

        System.out.println("\n--- Performing Operations ---");
        c.resize(2.0);
        r.rotate(45);
        t.resize(1.5);
        canvas.drawOnCanvas();
    }
}
