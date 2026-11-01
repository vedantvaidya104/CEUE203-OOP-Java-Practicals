import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public class MiniTestRunner {
    public static void main(String[] args) throws Exception {
        Class<?> cls = SampleTests.class;
        Object instance = cls.getDeclaredConstructor().newInstance();

        int ran = 0, passed = 0, failed = 0;
        System.out.println("Running @Run methods of " + cls.getSimpleName());

        for (Method m : cls.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Run.class)) {
                ran++;
                try {
                    m.invoke(instance);
                    passed++;
                } catch (Exception e) {
                    failed++;
                    System.out.println("  " + m.getName() + " FAILED: "
                            + e.getCause().getMessage());
                }
            }
        }
        System.out.println("Methods run: " + ran + " (passed " + passed
                + ", failed " + failed + ")");
    }
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

class SampleTests {
    @Run
    public void testAddition() {
        int result = 2 + 3;
        System.out.println("  testAddition -> 2 + 3 = " + result);
    }

    @Run
    public void testStringLength() {
        System.out.println("  testStringLength -> \"java\".length() = "
                + "java".length());
    }

    @Run
    public void testFailure() {
        throw new IllegalStateException("deliberate failure");
    }

    public void helperMethod() {
        System.out.println("  helperMethod should NOT run");
    }

    public void notMarked() {
        System.out.println("  notMarked should NOT run");
    }
}
