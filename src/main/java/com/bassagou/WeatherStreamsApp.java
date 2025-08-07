package com.bassagou;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Properties;

public class WeatherStreamsApp {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "weather-streams-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,  Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsBuilder builder = new StreamsBuilder();

        // 1. Lire les données du topic "weather-data"
        KStream<String, String> weatherstream = builder.stream("weather-data");

        KStream<String, WeatherRecord> parsedStream = weatherstream
                .mapValues(WeatherRecord::parse)
                .filter((key, record) -> record != null && record.temperatureCelsius>30)
                .mapValues(WeatherRecord::convertToFahrenheit);

        // 2. Grouper par station
        KGroupedStream<String, WeatherRecord> groupedByStation = parsedStream
                .groupBy((key, record) -> record.station, Grouped.with(Serdes.String(), new WeatherRecordSerde()));

// 3. Calculer la moyenne de température et d'humidité
        KTable<String, WeatherAverages> averages = groupedByStation.aggregate(
                WeatherAverages::new,
                (key, record, agg) -> agg.add(record),
                Materialized.with(Serdes.String(), new WeatherAveragesSerde())
        );

        // 4. Écrire les résultats dans le topic "station-averages"
        averages.toStream().mapValues(WeatherAverages::toString)
                .to("station-averages", Produced.with(Serdes.String(), Serdes.String()));

        // Lancer l'application
        KafkaStreams streams = new KafkaStreams(builder.build(), props);

        // Hook pour arrêt propre
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        streams.start();


    }
}
