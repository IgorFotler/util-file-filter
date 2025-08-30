package ru.shift.filefilter;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.IntSummaryStatistics;
import java.util.DoubleSummaryStatistics;

@Getter
public class Statistics {
    private final List<Integer> integers = new ArrayList<>();
    private final List<Double> floats = new ArrayList<>();
    private final List<String> strings = new ArrayList<>();

    public void addInteger(int value) {
        integers.add(value);
    }

    public void addFloat(double value) {
        floats.add(value);
    }

    public void addString(String value) {
        strings.add(value);
    }

    public String getShortStats() {
        return String.format("Целые числа: count = %d, Вещественные числа: count = %d, Строки: count = %d",
            integers.size(), floats.size(), strings.size());
    }

    public String getFullStats() {
        StringBuilder stats = new StringBuilder();

        if (!integers.isEmpty()) {
            IntSummaryStatistics intStats = integers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
            stats.append(String.format("Целые числа (count = %d): min = %d, max = %d, sum = %d, average = %.2f%n",
                    intStats.getCount(), intStats.getMin(), intStats.getMax(),
                    intStats.getSum(), intStats.getAverage()));
        }

        if (!floats.isEmpty()) {
            DoubleSummaryStatistics floatStats = floats.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();
            stats.append(String.format("Вещественные числа (count = %d): min = %.2f, max = %.2f, sum = %.2f, average = %.2f%n",
                floatStats.getCount(), floatStats.getMin(), floatStats.getMax(),
                floatStats.getSum(), floatStats.getAverage()));
        }

        if (!strings.isEmpty()) {
            int minLength = strings.stream()
                .mapToInt(String::length)
                .min()
                .orElse(0);
            int maxLength = strings.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
            stats.append(String.format("Строки (count = %d): min_length = %d, max_length = %d%n",
                strings.size(), minLength, maxLength));
        }
        return stats.toString();
    }
}
