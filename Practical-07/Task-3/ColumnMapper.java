import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class ColumnMapper {

    static <T> T populate(Class<T> cls, String[] header, String[] row) throws Exception {
        T obj = cls.getDeclaredConstructor().newInstance();

        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            String colName = field.getAnnotation(Column.class).name();

            int index = -1;
            for (int i = 0; i < header.length; i++) {
                if (header[i].trim().equalsIgnoreCase(colName)) {
                    index = i;
                    break;
                }
            }

            // Missing column: keep the default value and report it
            if (index == -1 || index >= row.length) {
                System.out.println("  [warning] column '" + colName
                        + "' not found - default kept");
                continue;
            }

            String text = row[index].trim();
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == int.class) {
                field.setInt(obj, Integer.parseInt(text));
            } else if (type == double.class) {
                field.setDouble(obj, Double.parseDouble(text));
            } else {
                field.set(obj, text);
            }
        }
        return obj;
    }

    public static void main(String[] args) throws Exception {
        String[] header = {"id", "name", "marks", "city"};
        String[] row = {"101", "Riya", "86.5", "Anand"};
        System.out.println("Full header:");
        System.out.println("  " + populate(Student.class, header, row));

        String[] shortHeader = {"name", "id", "marks"};
        String[] shortRow = {"Karan", "102", "74"};
        System.out.println("Header with missing 'city' column (different order):");
        System.out.println("  " + populate(Student.class, shortHeader, shortRow));
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

class Student {
    @Column(name = "id")
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "marks")
    double marks;

    @Column(name = "city")
    String city;

    @Override
    public String toString() {
        return "Student{id=" + id + ", name=" + name
                + ", marks=" + marks + ", city=" + city + "}";
    }
}
