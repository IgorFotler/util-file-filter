package ru.shift.filefilter;

import lombok.Getter;
import lombok.Setter;

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
                    config.setOutputDirectory(Paths.get(args[++i]));
                    break;
                case "-p":
                    config.setPrefix(args[++i]);
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