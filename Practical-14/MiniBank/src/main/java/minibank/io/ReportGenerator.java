package minibank.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

// Practical 11: end-of-day summary built from every file in the logs folder
public final class ReportGenerator {
    private ReportGenerator() { }

    public static String generate(Path logsDir) throws IOException {
        int total = 0, deposits = 0, withdrawals = 0, transfers = 0;
        if (Files.isDirectory(logsDir)) {
            try (DirectoryStream<Path> files =
                    Files.newDirectoryStream(logsDir, "*.log")) {
                for (Path file : files) {
                    try (BufferedReader reader = Files.newBufferedReader(file)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            total++;
                            if (line.contains("DEPOSIT")) deposits++;
                            else if (line.contains("WITHDRAW")) withdrawals++;
                            else if (line.contains("TRANSFER")) transfers++;
                        }
                    }
                }
            }
        }
        return "=== END-OF-DAY REPORT ===\n"
             + "Total transactions : " + total + "\n"
             + "Deposits           : " + deposits + "\n"
             + "Withdrawals        : " + withdrawals + "\n"
             + "Transfers          : " + transfers;
    }
}
