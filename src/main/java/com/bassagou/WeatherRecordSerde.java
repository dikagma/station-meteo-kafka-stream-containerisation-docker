package com.bassagou;

public class WeatherRecordSerde extends JsonSerde<WeatherRecord> {
    public WeatherRecordSerde() {
        super(WeatherRecord.class);
    }
}
