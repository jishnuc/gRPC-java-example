package com.github.jishnuc.grpc.calculator.client;

import com.github.jishnuc.proto.CalculatorServiceGrpc;
import com.github.jishnuc.proto.SumRequest;
import com.github.jishnuc.proto.SumResponse;
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

        System.out.println("Result ->"+response.getResult());

    }
}
