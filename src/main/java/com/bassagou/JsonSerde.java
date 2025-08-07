package com.bassagou;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonSerde<T> implements Serde<T> {
    private final Class<T> type;
    private final Gson gson = new Gson();

    public JsonSerde(Class<T> type) {
        this.type = type;
    }
    @Override
    public void close() {
        Serde.super.close();
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
    }

    @Override
    public Deserializer<T> deserializer() {
        return (topic, data) -> gson.fromJson(new String(data, StandardCharsets.UTF_8), type);
    }

    @Override
    public Serializer<T> serializer() {
        return (topic, data) -> gson.toJson(data).getBytes(StandardCharsets.UTF_8);
    }
}
