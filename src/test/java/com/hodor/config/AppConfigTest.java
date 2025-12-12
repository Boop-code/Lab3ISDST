package com.hodor.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void testOfCreatesValidConfig(@TempDir Path tempDir) {
        AppConfig config = AppConfig.of(tempDir, "ERROR", true, ".log", ".txt");

        assertEquals(tempDir, config.getScanDirectory());
        assertEquals("ERROR", config.getKeyword());
        assertTrue(config.isCaseSensitive());
        assertEquals(Set.of(".log", ".txt"), config.getFileExtensions());
    }
}