package ru.yandex.practicum.kafka.telemetry.serialization;

import com.google.protobuf.GeneratedMessageV3;
import org.apache.kafka.common.serialization.Serializer;

public class SensorGrpcSerializer implements Serializer<GeneratedMessageV3> {
    @Override
    public byte[] serialize(String topic, GeneratedMessageV3 object) {
        byte[] result = null;

//        if (object != null) {
//            if (object instanceof SensorEventProto)
//              result = ((SensorEventProto) object).toByteArray();
//            else //if (object instanceof HubEventProto)
//              //result = ((HubEventProto) object).toByteArray();
//              result = object.toByteArray();

            result = object.toByteArray();
//        }
        return result;
    }
}


//public class SensorGrpcSerializer implements Serializer<SensorEventProto> {
//    @Override
//    public byte[] serialize(String topic, SensorEventProto object) {
//        byte[] result = null;
//
//        if (object != null) {
////            if (object instanceof SensorEventProto)
////              result = ((SensorEventProto) object).toByteArray();
////            else //if (object instanceof HubEventProto)
////              //result = ((HubEventProto) object).toByteArray();
////              result = object.toByteArray();
//
//            result = object.toByteArray();
//        }
//        return result;
//    }
//}

