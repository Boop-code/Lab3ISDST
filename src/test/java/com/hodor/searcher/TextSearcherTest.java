package com.hodor.searcher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для TextSearcher.
 */
public class TextSearcherTest {

    @Test
    void testContainsKeyword_CaseSensitive_Found(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        try (FileWriter w = new FileWriter(file.toFile())) {
            w.write("This is a CRITICAL error\n");
            w.write("Another line\n");
        }

        TextSearcher searcher = new TextSearcher("CRITICAL", true);
        assertTrue(searcher.containsKeyword(file));
    }

    @Test
    void testContainsKeyword_CaseSensitive_NotFound(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        try (FileWriter w = new FileWriter(file.toFile())) {
            w.write("This is a critical error\n");
        }

        TextSearcher searcher = new TextSearcher("CRITICAL", true);
        assertFalse(searcher.containsKeyword(file));
    }

    @Test
    void testContainsKeyword_CaseInsensitive_Found(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        try (FileWriter w = new FileWriter(file.toFile())) {
            w.write("This is a critical error\n");
        }

        TextSearcher searcher = new TextSearcher("CRITICAL", false);
        assertTrue(searcher.containsKeyword(file));
    }

    @Test
    void testContainsKeyword_EmptyFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.createFile(file);

        TextSearcher searcher = new TextSearcher("CRITICAL", false);
        assertFalse(searcher.containsKeyword(file));
    }
}