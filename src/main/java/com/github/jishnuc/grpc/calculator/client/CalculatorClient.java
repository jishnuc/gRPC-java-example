package com.github.jishnuc.grpc.calculator.client;

import com.github.jishnuc.grpc.calculator.server.CalculatorService;
import com.github.jishnuc.proto.*;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CalculatorClient {

    public static void main(String[] args) {
        System.out.println("Starting Client");
        CalculatorClient calculator=new CalculatorClient();
        calculator.run();
    }
    public void run(){
        ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost",50051)
                .usePlaintext()
                .build();

        doSum(channel,45,56);
        doPrimeNumberDecomposition(channel,120);
        doComputeAverage(channel,23,11,44,66,88,99);
    }

    private void doComputeAverage(ManagedChannel channel, int... values) {
        CalculatorServiceGrpc.CalculatorServiceStub asyncClient=CalculatorServiceGrpc.newStub(channel);
        CountDownLatch latch=new CountDownLatch(1);
        StreamObserver<ComputeAverageRequest> requestStreamObserver = asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
            @Override
            public void onNext(ComputeAverageResponse value) {
                System.out.println("Server calculated average is " + value.getAverage());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.out.println("Server calculation completed");
                latch.countDown();
            }
        });
        for(int val:values){
            System.out.println("Sending value "+val+" to server");
            requestStreamObserver.onNext(ComputeAverageRequest
                                                .newBuilder()
                                                .setValue(val)
                                                .build());
        }
        requestStreamObserver.onCompleted();
        System.out.println("Server send completed");
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doPrimeNumberDecomposition(ManagedChannel channel, int number) {
        CalculatorServiceGrpc.CalculatorServiceBlockingStub client=CalculatorServiceGrpc.newBlockingStub(channel);
        System.out.println("------Prime Number Decomposition----");
        client.primeNumberDecompose(PrimeNumberDecomposeRequest.newBuilder()
                .setNumber(120)
                .build())
                .forEachRemaining(primeNumberDecomposeResponse -> System.out.println(primeNumberDecomposeResponse.getPrimeFactor()));
    }

    private void doSum(ManagedChannel channel, int op1, int op2) {
        SumRequest request=SumRequest.newBuilder()
                .setOp1(op1)
                .setOp2(op2)
                .build();

        CalculatorServiceGrpc.CalculatorServiceBlockingStub client=CalculatorServiceGrpc.newBlockingStub(channel);

        SumResponse response = client.sum(request);
        System.out.println("Sum Result ->"+response.getResult());
    }


}
