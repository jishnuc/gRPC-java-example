package com.github.jishnuc.grpc.calculator.client;

import com.github.jishnuc.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CalculatorClient {
    public static void main(String[] args) {
        System.out.println("Starting Client");
        ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost",50051)
                                                    .usePlaintext()
                                                    .build();

        SumRequest request=SumRequest.newBuilder()
                                                    .setOp1(64)
                                                    .setOp2(49)
                                                    .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub client=CalculatorServiceGrpc.newBlockingStub(channel);

        SumResponse response = client.sum(request);
        System.out.println("Sum Result ->"+response.getResult());

        System.out.println("------Prime Number Decomposition----");
        client.primeNumberDecompose(PrimeNumberDecomposeRequest.newBuilder()
                .setNumber(120)
                .build())
                .forEachRemaining(primeNumberDecomposeResponse -> System.out.println(primeNumberDecomposeResponse.getPrimeFactor()));




    }
}
