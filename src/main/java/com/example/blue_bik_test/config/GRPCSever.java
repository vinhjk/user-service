package com.example.blue_bik_test.config;

import com.example.blue_bik_test.service.UserService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class GRPCSever {
    @Autowired
    private UserService userService;

    @Value("${grpc.server.port}")
    private int port;

    private Server server;

    public void start() throws IOException {
        log.info("Starting gRPC on port {}.", port);
        server = ServerBuilder.forPort(port).addService(userService).build().start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutting down gRPC server.");
            this.stop();
            log.info("gRPC server shut down successfully.");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    public void block() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
