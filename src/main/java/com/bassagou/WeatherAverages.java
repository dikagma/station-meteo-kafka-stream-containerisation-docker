package com.bassagou;

public class WeatherAverages {
    public double totalTemp = 0;
    public double totalHumidity = 0;
    public long count = 0;

    public WeatherAverages add(WeatherRecord r) {
        totalTemp += r.temperatureCelsius;
        totalHumidity += r.humidity;
        count++;
        return this;
    }

    public String toString() {
        if (count == 0) return "No data";
        double avgTemp = totalTemp / count;
        double avgHumidity = totalHumidity / count;
        return String.format("Température Moyenne = %.2f°F, Humidité Moyenne = %.2f%%", avgTemp, avgHumidity);
    }
}
