package com.hodor.scanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Класс для рекурсивного сканирования директории и отбора файлов по заданным расширениям.
 */
public class FileScanner {

    private final Path rootDirectory;
    private final Set<String> allowedExtensions;

    /**
     * Создаёт сканер файлов.
     *
     * @param rootDirectory директория, с которой начинается рекурсивный обход
     * @param allowedExtensions множество разрешённых расширений файлов (например: {".txt", ".log"})
     * @throws IllegalArgumentException если rootDirectory не существует или не является директорией
     */
    public FileScanner(Path rootDirectory, Set<String> allowedExtensions) {
        if (!Files.exists(rootDirectory)) {
            throw new IllegalArgumentException("Директория не существует: " + rootDirectory);
        }
        if (!Files.isDirectory(rootDirectory)) {
            throw new IllegalArgumentException("Указанный путь не является директорией: " + rootDirectory);
        }
        this.rootDirectory = rootDirectory;
        this.allowedExtensions = allowedExtensions;
    }

    /**
     * Выполняет рекурсивный обход директории и возвращает список файлов,
     * расширение которых содержится в allowedExtensions.
     *
     * @return список путей к подходящим файлам
     * @throws RuntimeException если произошла ошибка при чтении файловой системы
     */
    public List<Path> scan() {
        List<Path> matchedFiles = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(rootDirectory)) {
            for (Path path : paths.toList()) {
                if (Files.isRegularFile(path) && hasAllowedExtension(path)) {
                    matchedFiles.add(path);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сканировании директории: " + rootDirectory, e);
        }
        return matchedFiles;
    }

    /**
     * Проверяет, имеет ли файл одно из разрешённых расширений.
     *
     * @param filePath путь к файлу
     * @return true, если расширение разрешено, иначе false
     */
    private boolean hasAllowedExtension(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        for (String ext : allowedExtensions) {
            if (fileName.endsWith(ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}