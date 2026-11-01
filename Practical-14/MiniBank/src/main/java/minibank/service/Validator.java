package minibank.service;

import minibank.model.InvalidInputException;

public final class Validator {
    private Validator() { }

    public static void requireName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Owner name must not be empty");
        }
    }

    public static void requirePositive(double amount) {
        if (Double.isNaN(amount) || amount <= 0) {
            throw new InvalidInputException(
                "Amount must be greater than zero: " + amount);
        }
    }
}
