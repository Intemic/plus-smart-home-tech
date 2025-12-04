package ru.yandex.practicum.telemetry.analyzer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("analyzer.grpc")
public class GrpcConfig {
    private GrpcClientConfig client;

    @Getter
    @Setter
    public static class GrpcClientConfig {
        private GrpcClientHubConfig hubRouter;
    }

    @Getter
    @Setter
    public static class GrpcClientHubConfig {
        private String address;
        private boolean enableKeepAlive;
        private boolean keepAliveWithoutCalls;
        private String negotiationType;
    }
}
