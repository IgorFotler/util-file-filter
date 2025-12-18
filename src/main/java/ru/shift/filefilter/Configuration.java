package ru.shift.filefilter;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Configuration {
    private Path outputDirectory = Paths.get(".");
    private String prefix = "";
    private boolean append = false;
    private StatsOption statsOption;
    private final List<Path> inputFiles = new ArrayList<>();

    public static Configuration parse(String[] args) {
        Configuration config = new Configuration();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "-o":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("Для опции -о необходимо указать путь к директории.");
                    }
                    String outputDirStr = args[++i];
                    if (outputDirStr.equals("-p") || outputDirStr.equals("-a") || outputDirStr.equals("-s") ||
                            outputDirStr.equals("-f")) {
                        throw new IllegalArgumentException("Для опции -о необходимо указать путь к директории.");
                    }
                    Path outputDir = Paths.get(outputDirStr);
                    if (!Files.exists(outputDir)) {
                        throw new IllegalArgumentException("Неверно указан путь: " + outputDirStr);
                    }
                    if (!Files.isDirectory(outputDir)) {
                        throw new IllegalArgumentException("Указанный путь не является директорией: " + outputDirStr);
                    }
                    config.setOutputDirectory(Paths.get(outputDirStr));
                    break;

                case "-p":
                    if (i + 1 >= args.length) {
                        throw new IllegalArgumentException("Для опции -р необходимо указать префикс.");
                    }
                    String prefixStr = args[++i];
                    if (prefixStr.equals("-o") || prefixStr.equals("-a") || prefixStr.equals("-s") ||
                            prefixStr.equals("-f")) {
                        throw new IllegalArgumentException("Для опции -р необходимо указать префикс.");
                    }
                    if (prefixStr.matches(".*[\\\\/|:?<>*\"].*")) {
                        throw new IllegalArgumentException("Введен недопустимый символ для префикса. Недопустимые символы: \\ / | : ? < > \" *");
                    }
                    config.setPrefix(prefixStr);
                    break;

                case "-a":
                    config.setAppend(true);
                    break;

                case "-s":
                    config.setStatsOption(StatsOption.SHORT);
                    break;

                case "-f":
                    config.setStatsOption(StatsOption.FULL);
                    break;

                default:
                    if (arg.startsWith("-")) {
                        throw new IllegalArgumentException("Неизвестная опция: " + arg);
                    }
                    config.inputFiles.add(Paths.get(args[i]));
            }
        }

        if (config.inputFiles.isEmpty()) {
            throw new IllegalArgumentException("Входные файлы не указаны.");
        }
        return config;
    }
}