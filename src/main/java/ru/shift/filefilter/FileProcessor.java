package ru.shift.filefilter;

import lombok.RequiredArgsConstructor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@RequiredArgsConstructor
public class FileProcessor {
    private final Configuration config;
    private final Statistics stats = new Statistics();
    private BufferedWriter integerWriter;
    private BufferedWriter floatWriter;
    private BufferedWriter stringWriter;
    private boolean integersFound = false;
    private boolean floatsFound = false;
    private boolean stringsFound = false;

    public void processFiles() throws IOException {
        try {
            for (Path inputFile : config.getInputFiles()) {
                if (!Files.exists(inputFile)) {
                    System.err.println("Файл - " + inputFile + " не найден.");
                    continue;
                }
                processSingleFile(inputFile);
            }
        } finally {
            closeWriters();
        }
    }

    private void processSingleFile(Path file) throws IOException {
        Files.lines(file).forEach(this::processLine);
    }

    private void processLine(String line) {
        try {
            line = line.trim();
            if (line.isEmpty()) {
                return;
            }

            if (line.matches("-?\\d+")) {
                processInteger(line);
            } else if (line.matches("-?\\d+\\.\\d+([eE][+-]?\\d+)?")) {
                processFloat(line);
            } else {
                processString(line);
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования числа: " + line);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    private void processInteger(String line) throws IOException {
        if (!integersFound) {
            integerWriter = initializeWriter("integers.txt");
            integersFound = true;
        }

        long value = Long.parseLong(line);
        stats.addInteger(value);
        integerWriter.write(line);
        integerWriter.newLine();
    }

    private void processFloat(String line) throws IOException {
        if (!floatsFound) {
            floatWriter = initializeWriter("floats.txt");
            floatsFound = true;
        }

        double value = Double.parseDouble(line);
        stats.addFloat(value);
        floatWriter.write(line);
        floatWriter.newLine();
    }

    private void processString(String line) throws IOException {
        if (!stringsFound) {
            stringWriter = initializeWriter("strings.txt");
            stringsFound = true;
        }

        stats.addString(line);
        stringWriter.write(line);
        stringWriter.newLine();
    }

    private BufferedWriter initializeWriter(String filename) throws IOException {
        StandardOpenOption mode;
        if (config.isAppend()) {
            mode = StandardOpenOption.APPEND;
        } else {
            mode = StandardOpenOption.TRUNCATE_EXISTING;
        }

        return Files.newBufferedWriter(
                config.getOutputDirectory().resolve(config.getPrefix() + filename),
                StandardOpenOption.CREATE, mode
        );
    }

    private void closeWriters() {
        closeWriter(integerWriter, "integer");
        closeWriter(floatWriter, "float");
        closeWriter(stringWriter, "string");
    }

    private void closeWriter(BufferedWriter writer, String type) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии " + type + "Writer: " + e.getMessage());
            }
        }
    }

    public void printStatistics(StatsOption statsOption) {
        if (statsOption == StatsOption.SHORT) {
            System.out.println(stats.getShortStats());
        } else if (statsOption == StatsOption.FULL) {
            System.out.println(stats.getFullStats());
        }
    }
}