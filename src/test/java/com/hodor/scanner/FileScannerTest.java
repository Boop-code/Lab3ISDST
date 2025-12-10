package com.hodor.scanner;

import com.hodor.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для FileScanner.
 */
public class FileScannerTest {

    @Test
    void testScanFindsCorrectFiles(@TempDir Path tempDir) throws IOException {
        // Создаём структуру:
        // tempDir/
        //   file1.txt
        //   file2.log
        //   file3.pdf
        //   subdir/
        //     file4.txt

        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.log"));
        Files.createFile(tempDir.resolve("file3.pdf"));
        Files.createDirectory(tempDir.resolve("subdir"));
        Files.createFile(tempDir.resolve("subdir/file4.txt"));

        Set<String> extensions = Set.of(".txt", ".log");
        FileScanner scanner = new FileScanner(tempDir, extensions);
        List<Path> result = scanner.scan();

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(p -> p.endsWith("file1.txt")));
        assertTrue(result.stream().anyMatch(p -> p.endsWith("file2.log")));
        assertTrue(result.stream().anyMatch(p -> p.endsWith("file4.txt")));
    }

    @Test
    void testScanReturnsEmptyOnNoMatchingFiles(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("notes.pdf"));
        Files.createFile(tempDir.resolve("image.png"));

        FileScanner scanner = new FileScanner(tempDir, Set.of(".txt"));
        List<Path> result = scanner.scan();

        assertTrue(result.isEmpty());
    }
}