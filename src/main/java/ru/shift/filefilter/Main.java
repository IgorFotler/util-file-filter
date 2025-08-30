package ru.shift.filefilter;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration config = Configuration.parse(args);
            FileProcessor fileProcessor = new FileProcessor(config);
            fileProcessor.processFiles();
            fileProcessor.printStatistics(config.getStatsOption());
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            System.exit(1);
        }
    }
}
