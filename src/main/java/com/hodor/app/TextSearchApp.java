package com.hodor.app;

import com.hodor.config.AppConfig;
import com.hodor.scanner.FileScanner;
import com.hodor.searcher.TextSearcher;

import java.nio.file.Path;
import java.util.List;

/**
 * Главный класс приложения для поиска ключевого слова в файлах.
 * <p>
 * Приложение:
 * <ul>
 *   <li>Рекурсивно сканирует директорию из app.properties</li>
 *   <li>Фильтрует файлы по расширениям</li>
 *   <li>Ищет ключевое слово с учётом/игнорированием регистра</li>
 *   <li>Выводит абсолютные пути найденных файлов в stdout</li>
 * </ul>
 */
public class TextSearchApp {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            // 1. Загрузка конфигурации
            AppConfig config = AppConfig.fromClasspath();

            // 2. сканирование файлов
            FileScanner scanner = new FileScanner(config.getScanDirectory(), config.getFileExtensions());
            List<Path> files = scanner.scan();

            // 3. Поиск по файлам
            TextSearcher searcher = new TextSearcher(config.getKeyword(), config.isCaseSensitive());

            // 4. Вывод найденных файлов (без дубликатов — каждый файл проверяется один раз)
            for (Path file : files) {
                if (searcher.containsKeyword(file)) {
                    System.out.println(file.toAbsolutePath().normalize());
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении приложения: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}