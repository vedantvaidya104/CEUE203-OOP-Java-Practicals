import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FolderReport {

    public static void main(String[] args) throws IOException {
        // Build a small sample folder tree
        Path root = Files.createTempDirectory("project");
        Files.createDirectories(root.resolve("src/main"));
        Files.createDirectories(root.resolve("docs"));
        Files.writeString(root.resolve("readme.txt"), "MiniBank project");
        Files.writeString(root.resolve("src/App.java"), "public class App { }");
        Files.writeString(root.resolve("src/main/Bank.java"), "class Bank { int x; }");
        Files.writeString(root.resolve("docs/notes.md"), "# Notes\nSome notes here.");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        List<String> report = new ArrayList<>();
        long[] totals = new long[2];   // [0] file count, [1] total bytes

        report.add(String.format("%-28s %8s  %s", "File", "Size", "Last modified"));

        // Walk the whole tree
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                report.add(String.format("%-28s %8d  %s",
                        root.relativize(file).toString(),
                        attrs.size(),
                        fmt.format(attrs.lastModifiedTime().toInstant())));
                totals[0]++;
                totals[1] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });

        report.add("----------------------------------------------------------");
        report.add("Files: " + totals[0] + "   Total size: " + totals[1] + " bytes");

        // Write the summary to a file, then print it
        Path out = Files.createTempFile("folder-report", ".txt");
        Files.write(out, report);

        System.out.println("Report written to a file; its contents:\n");
        for (String line : Files.readAllLines(out)) {
            System.out.println(line);
        }

        // Clean up
        Files.deleteIfExists(out);
        try (var paths = Files.walk(root)) {
            paths.sorted(java.util.Comparator.reverseOrder())
                 .forEach(p -> p.toFile().delete());
        }
    }
}
