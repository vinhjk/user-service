package com.example.blue_bik_test.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrpcCommandLineRunner implements CommandLineRunner {
    private final GRPCSever grpcSever;

    @Override
    public void run(String... args) throws Exception {
        grpcSever.start();
        grpcSever.block();
    }
}
