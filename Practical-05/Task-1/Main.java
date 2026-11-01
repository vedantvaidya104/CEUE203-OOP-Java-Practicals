abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    double length;
    double width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    double area() {
        return length * width;
    }
}

class Triangle extends Shape {
    double base;
    double height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    double area() {
        return 0.5 * base * height;
    }
}

public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(5.0),
            new Rectangle(4.0, 6.0),
            new Triangle(3.0, 8.0)
        };

        double total = 0.0;
        double largest = 0.0;

        for (Shape s : shapes) {
            double area = s.area();
            total += area;
            if (area > largest) {
                largest = area;
            }
            System.out.println(s.getClass().getSimpleName() + " area = "
                + String.format("%.2f", area)
                + " | running total = " + String.format("%.2f", total));
        }

        System.out.println("Total area   = " + String.format("%.2f", total));
        System.out.println("Largest area = " + String.format("%.2f", largest));
    }
}
