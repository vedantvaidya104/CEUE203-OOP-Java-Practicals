package util;

public class Validator {
    public static boolean isPositive(long amount) {
        return amount > 0;
    }

    public static boolean isValidChoice(int choice, int max) {
        return choice >= 1 && choice <= max;
    }
}
