import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

public class LogAnalyzer {

    public static void main(String[] args) throws IOException {
        Path dir = Files.createTempDirectory("logs");

        Path f1 = dir.resolve("app1.log");
        Path f2 = dir.resolve("app2.log");
        Path f3 = dir.resolve("app3.log");

        Files.write(f1, Arrays.asList(
            "INFO  server started",
            "ERROR disk full",
            "INFO  user login",
            "ERROR timeout"));
        Files.write(f2, Arrays.asList(
            "INFO  backup done",
            "WARN  low memory",
            "ERROR null pointer"));
        Files.write(f3, Arrays.asList(
            "INFO  idle",
            "INFO  shutdown"));

        String keyword = "ERROR";
        int totalLines = 0;
        int keywordLines = 0;

        for (Path p : Arrays.asList(f1, f2, f3)) {
            List<String> lines = Files.readAllLines(p);
            int fileKeyword = 0;
            for (String line : lines) {
                if (line.contains(keyword)) {
                    fileKeyword++;
                }
            }
            totalLines += lines.size();
            keywordLines += fileKeyword;

            BasicFileAttributes attrs =
                    Files.readAttributes(p, BasicFileAttributes.class);
            System.out.printf("%-10s lines=%d  '%s' lines=%d  size=%d bytes%n",
                    p.getFileName(), lines.size(), keyword,
                    fileKeyword, attrs.size());
        }

        System.out.println("-----------------------------------------------");
        System.out.println("Total lines across all files   : " + totalLines);
        System.out.println("Lines containing '" + keyword + "'     : " + keywordLines);

        for (Path p : Arrays.asList(f1, f2, f3)) {
            Files.deleteIfExists(p);
        }
        Files.deleteIfExists(dir);
    }
}
