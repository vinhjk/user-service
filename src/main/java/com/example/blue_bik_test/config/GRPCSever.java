package com.example.blue_bik_test.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GRPCSever {

    @Value("${grpc.server.port}")
    private int port;
}
