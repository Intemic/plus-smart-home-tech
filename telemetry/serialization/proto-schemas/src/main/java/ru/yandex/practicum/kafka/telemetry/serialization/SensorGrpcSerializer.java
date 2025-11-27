package ru.yandex.practicum.kafka.telemetry.serialization;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.kafka.common.serialization.Serializer;

public class SensorGrpcSerializer implements Serializer<GeneratedMessageV3> {
    @Override
    public byte[] serialize(String topic, GeneratedMessageV3 object) {
        byte[] result = null;
        if (object != null) {
           result = object.toByteArray();
        }
        return result;
    }
}
