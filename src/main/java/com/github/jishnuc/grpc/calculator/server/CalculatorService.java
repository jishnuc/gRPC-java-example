package com.github.jishnuc.grpc.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorService {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting Server");
        //setup server
        Server server= ServerBuilder.forPort(50051)
                                    .addService(new CalculatorServiceImpl())
                                    .build();
        //start the server
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("Received shutdown request");
            server.shutdown();
            System.out.println("Successfully stopped the server");
        }));
        server.awaitTermination();
    }
}
