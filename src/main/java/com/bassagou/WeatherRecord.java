package com.bassagou;

public class WeatherRecord {
        public String station;
        public double temperatureCelsius;
        public double humidity;

        public WeatherRecord(String station, double temperatureCelsius, double humidity) {
            this.station = station;
            this.temperatureCelsius = temperatureCelsius;
            this.humidity = humidity;
        }

        public static WeatherRecord parse(String value) {
            try {
                String[] parts = value.split(",");
                String station = parts[0];
                double temp = Double.parseDouble(parts[1]);
                double humidity = Double.parseDouble(parts[2]);
                return new WeatherRecord(station, temp, humidity);
            } catch (Exception e) {
                return null;
            }
        }

        public WeatherRecord convertToFahrenheit() {
            double tempF = (temperatureCelsius * 9.0 / 5.0) + 32;
            return new WeatherRecord(station, tempF, humidity);
        }
}


