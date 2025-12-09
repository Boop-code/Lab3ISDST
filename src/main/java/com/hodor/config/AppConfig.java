package com.hodor.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для загрузки и хранения конфигурации приложения из app.properties.
 * <p>
 * Поддерживаемые параметры:
 * <ul>
 *   <li>{@code scan.directory} — директория для сканирования</li>
 *   <li>{@code search.keyword} — ключевое слово для поиска</li>
 *   <li>{@code search.case.sensitive} — учитывать ли регистр</li>
 *   <li>{@code scan.file.extensions} — расширения файлов через запятую (например: .txt, .log)</li>
 * </ul>
 */
public class AppConfig {

    private final Path scanDirectory;
    private final String keyword;
    private final boolean caseSensitive;
    private final Set<String> fileExtensions;

    /**
     * Загружает конфигурацию из файла app.properties в classpath.
     *
     * @throws RuntimeException если файл конфигурации не найден или содержит ошибки
     */
    public AppConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("app.properties")) {
            if (input == null) {
                throw new RuntimeException("Файл конфигурации app.properties не найден в classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении app.properties", e);
        }

        this.scanDirectory = Paths.get(props.getProperty("scan.directory", "./data"));
        this.keyword = props.getProperty("search.keyword", "");
        if (this.keyword.isEmpty()) {
            throw new RuntimeException("Параметр search.keyword не задан в app.properties");
        }
        this.caseSensitive = Boolean.parseBoolean(props.getProperty("search.case.sensitive", "false"));
        String extensions = props.getProperty("scan.file.extensions", ".txt");
        this.fileExtensions = Stream.of(extensions.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Возвращает путь к директории, которую необходимо сканировать.
     *
     * @return путь к директории
     */
    public Path getScanDirectory() {
        return scanDirectory;
    }

    /**
     * Возвращает ключевое слово, которое ищется в файлах.
     *
     * @return ключевое слово
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Возвращает флаг учёта регистра при поиске.
     *
     * @return {@code true}, если поиск чувствителен к регистру, иначе {@code false}
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * Возвращает множество допустимых расширений файлов для сканирования.
     *
     * @return множество расширений (например: {".txt", ".log"})
     */
    public Set<String> getFileExtensions() {
        return fileExtensions;
    }
}