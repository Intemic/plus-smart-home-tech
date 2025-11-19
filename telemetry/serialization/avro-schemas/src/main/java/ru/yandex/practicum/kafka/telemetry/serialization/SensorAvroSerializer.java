package ru.yandex.practicum.kafka.telemetry.serialization;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SensorAvroSerializer implements Serializer<SpecificRecordBase> {
    private EncoderFactory encoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    public byte[] serialize(String topic, SpecificRecordBase object) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] result = null;
            if (object != null) {
                encoder = encoderFactory.binaryEncoder(outputStream, null);
                DatumWriter<SpecificRecordBase> writer = new SpecificDatumWriter<>(object.getSchema());
                writer.write(object, encoder);
                encoder.flush();
                result = outputStream.toByteArray();
            }

            return result;
        } catch (IOException ex) {
            throw new RuntimeException("Ошибка сериализации для топика %s".formatted(topic), ex);
        }
    }
}
