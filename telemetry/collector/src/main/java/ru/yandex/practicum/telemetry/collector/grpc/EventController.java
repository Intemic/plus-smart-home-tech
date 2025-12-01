package ru.yandex.practicum.telemetry.collector.grpc;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.telemetry.collector.grpc.handler.hub.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.grpc.handler.sensor.SensorEventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventController(Set<SensorEventHandler> sensorEventHandlers,
                           Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));

        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        if (!sensorEventHandlers.containsKey(request.getPayloadCase()))
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());

        sensorEventHandlers.get(request.getPayloadCase()).handle(request);
        // после обработки события возвращаем ответ клиенту
        responseObserver.onNext(Empty.getDefaultInstance());
        // и завершаем обработку запроса
        responseObserver.onCompleted();
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        if (!hubEventHandlers.containsKey(request.getPayloadCase()))
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());

        hubEventHandlers.get(request.getPayloadCase()).handle(request);
        // после обработки события возвращаем ответ клиенту
        responseObserver.onNext(Empty.getDefaultInstance());
        // и завершаем обработку запроса
        responseObserver.onCompleted();
    }
}
