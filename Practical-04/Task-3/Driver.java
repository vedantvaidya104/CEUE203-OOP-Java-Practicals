import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter template: ");
        String template = sc.nextLine();

        int n;

        while (true) {
            System.out.print("Enter number of placeholders: ");

            if (sc.hasNextInt()) {
                n = sc.nextInt();
                sc.nextLine(); // consume newline
                break;
            } else {
                System.out.println("Invalid input! Please enter an integer.");
                sc.next(); // discard invalid input
            }
        }

        String[] names = new String[n];
        String[] values = new String[n];

        for (int i = 0; i < n; i++) {

            System.out.print("Enter placeholder name: ");
            names[i] = sc.nextLine();

            System.out.print("Enter value: ");
            values[i] = sc.nextLine();
        }

        TemplateFiller tf = new TemplateFiller();

        System.out.println("\nFilled Template:");
        System.out.println(tf.fillTemplate(template, names, values));

        sc.close();
    }
}
