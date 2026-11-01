package minibank.io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import minibank.model.Account;

// Practical 11: save / load all accounts with Java serialization
public final class StatePersister {
    private StatePersister() { }

    public static void save(Account[] accounts, Path file)
            throws IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }
    }

    public static Account[] load(Path file)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) in.readObject();
        }
    }
}
