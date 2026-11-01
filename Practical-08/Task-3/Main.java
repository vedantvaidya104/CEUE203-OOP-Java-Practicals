import java.util.Scanner;

class OperationException extends Exception {

    public OperationException(String message) {
        super(message);
    }
}

class MyResource implements AutoCloseable {

    private String name;

    public MyResource(String name) {

        this.name = name;

        System.out.println(
            "Resource '" + name + "' opened."
        );
    }

    public void performOperation(boolean causeError)
            throws OperationException {

        System.out.println(
            "Performing operation using '" + name + "'..."
        );

        if (causeError) {

            throw new OperationException(
                "Original operation failed."
            );
        }

        System.out.println(
            "Operation completed successfully."
        );
    }

    @Override
    public void close() throws Exception {

        System.out.println(
            "Closing resource '" + name + "'..."
        );

        throw new Exception(
            "Exception occurred while closing the resource."
        );
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter resource name: ");
        String resourceName = sc.nextLine();

        System.out.print(
            "Do you want the operation to fail? (yes/no): "
        );

        String choice = sc.nextLine();

        boolean causeError =
            choice.equalsIgnoreCase("yes");

        try (MyResource resource =
                new MyResource(resourceName)) {

            resource.performOperation(causeError);

        } catch (Exception e) {

            System.out.println(
                "\nMain exception reported:"
            );

            System.out.println(
                e.getMessage()
            );

            Throwable[] suppressed =
                e.getSuppressed();

            if (suppressed.length > 0) {

                System.out.println(
                    "\nSuppressed exception(s):"
                );

                for (Throwable t : suppressed) {

                    System.out.println(
                        t.getMessage()
                    );
                }
            }
        }

        sc.close();
    }
}
