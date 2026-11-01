import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveLoad {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path file = Paths.get("students.dat");

        Student[] students = {
            new Student(1, "Riya", "secret123"),
            new Student(2, "Karan", "pass@456"),
            new Student(3, "Meera", "mypwd789")
        };

        System.out.println("Before saving:");
        for (Student s : students) {
            System.out.println("  " + s);
        }

        // Save the whole array with ObjectOutputStream
        try (ObjectOutputStream out =
                 new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(students);
        }
        System.out.println("\nSaved to " + file + " (" + Files.size(file) + " bytes)");

        // Read it back into fresh objects
        Student[] loaded;
        try (ObjectInputStream in =
                 new ObjectInputStream(Files.newInputStream(file))) {
            loaded = (Student[]) in.readObject();
        }

        System.out.println("\nAfter loading:");
        for (Student s : loaded) {
            System.out.println("  " + s);
        }

        System.out.println("\nSame object as before? " + (loaded[0] == students[0]));
        System.out.println("id and name survived, transient password is null/lost.");

        Files.deleteIfExists(file);
    }
}

class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private transient String password;   // transient -> NOT saved

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name + ", password=" + password + "}";
    }
}
