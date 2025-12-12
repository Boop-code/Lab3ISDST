package com.hodor.searcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс для поиска ключевого слова в текстовом файле.
 */
public class TextSearcher {

    private final String keyword;
    private final boolean caseSensitive;

    /**
     * создаёт экземпляр поиска.
     *
     * @param keyword ключевое слово для поиска
     * @param caseSensitive флаг: true — учитывать регистр, false — игнорировать
     * @throws IllegalArgumentException если ключевое слово пустое или null
     */
    public TextSearcher(String keyword, boolean caseSensitive) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Ключевое слово не может быть пустым или null");
        }
        this.keyword = caseSensitive ? keyword : keyword.toLowerCase();
        this.caseSensitive = caseSensitive;
    }

    /**
     * Ищет ключевое слово в указанном файле.
     * Поиск выполняется построчно.
     *
     * @param filePath путь к файлу
     * @return true, если слово найдено хотя бы в одной строке, иначе false
     * @throws RuntimeException если файл не может быть прочитан
     */
    public boolean containsKeyword(Path filePath) {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (matches(line)) {
                    return true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла: " + filePath, e);
        }
        return false;
    }

    /**
     * Проверяет, содержит ли строка ключевое слово с учётом регистра.
     *
     * @param line строка из файла
     * @return true, если найдено совпадение
     */
    private boolean matches(String line) {
        if (line == null) {
            return false;
        }
        String searchableLine = caseSensitive ? line : line.toLowerCase();
        return searchableLine.contains(keyword);
    }
}