package com.hodor.config;

import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для загрузки и хранения конфигурации приложения.
 */
public class AppConfig {

    private final Path scanDirectory;
    private final String keyword;
    private final boolean caseSensitive;
    private final Set<String> fileExtensions;

    /**
     * Приватный конструктор — используется только внутренними методами.
     */
    private AppConfig(Path scanDirectory, String keyword, boolean caseSensitive, Set<String> fileExtensions) {
        this.scanDirectory = scanDirectory;
        this.keyword = keyword;
        this.caseSensitive = caseSensitive;
        this.fileExtensions = fileExtensions;
    }

    /**
     * Загружает конфигурацию из app.properties в classpath.
     *
     * @return экземпляр AppConfig
     * @throws RuntimeException если файл не найден или параметры некорректны
     */
    public static AppConfig fromClasspath() {
        Properties props = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл app.properties не найден в classpath");
            }
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке app.properties", e);
        }

        Path dir = Paths.get(props.getProperty("scan.directory", "./data"));
        String keyword = props.getProperty("search.keyword", "");
        if (keyword.isEmpty()) {
            throw new RuntimeException("Параметр search.keyword не задан");
        }
        boolean caseSensitive = Boolean.parseBoolean(props.getProperty("search.case.sensitive", "false"));
        String exts = props.getProperty("scan.file.extensions", ".txt");
        Set<String> extensions = Stream.of(exts.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        return new AppConfig(dir, keyword, caseSensitive, extensions);
    }

    /**
     * Создаёт конфигурацию из явно заданных значений (для тестов).
     */
    public static AppConfig of(Path scanDirectory, String keyword, boolean caseSensitive, String... extensions) {
        return new AppConfig(
                scanDirectory,
                keyword,
                caseSensitive,
                Set.of(extensions)
        );
    }

    // === Геттеры (без изменений) ===

    public Path getScanDirectory() {
        return scanDirectory;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public Set<String> getFileExtensions() {
        return fileExtensions;
    }
}