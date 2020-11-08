package com.github.jishnuc.grpc.greeting.client;

import com.github.jishnuc.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {


    public static void main(String[] args) {
        System.out.println("GrPC client");
        GreetingClient client=new GreetingClient();
        client.run();

    }
    public void run(){
        ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost",50051)
                .usePlaintext()
                .build();

        doUnaryCall(channel);
        doServerStreamingCall(channel);
        doClientStreamingCall(channel);
        System.out.println("Shutdown");
        channel.shutdown();
    }

    private void doClientStreamingCall(ManagedChannel channel) {
        //create an asynchronous client
        GreetingServiceGrpc.GreetingServiceStub asyncClient=GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch=new CountDownLatch(1);
        StreamObserver<LongGreetRequest> requestStreamObserver = asyncClient.longGreet(new StreamObserver<LongGreetResponse>() {
            @Override
            public void onNext(LongGreetResponse value) {
                //we get a response from server
                //onNext will be called once
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                //we get a error from server
            }

            @Override
            public void onCompleted() {
                //we get a completed signal from server
                //onCompleted will be called right after onNext()
                System.out.println("Server has completed sending data");
                latch.countDown();
            }
        });
        //streaming message #1
        System.out.println("streaming message #1");
        requestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Jishnu")
                        .setLastName("Chatterjee")
                        .build())
                .build());
        //streaming message #2
        System.out.println("streaming message #2");
        requestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("John")
                        .setLastName("Hooper")
                        .build())
                .build());
        //streaming message #3
        System.out.println("streaming message #3");
        requestStreamObserver.onNext(LongGreetRequest
                .newBuilder()
                .setGreeting(Greeting.newBuilder()
                        .setFirstName("Lucy")
                        .setLastName("Perry")
                        .build())
                .build());
        //We tell the server that the client is done  sending data
        System.out.println("Done sending messaged to server");
        requestStreamObserver.onCompleted();

        try {
            System.out.println("Will wait for 3 sec to respond");
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void doServerStreamingCall(ManagedChannel channel) {
        Greeting greeting=Greeting.newBuilder()
                .setFirstName("Jishnu")
                .setLastName("Chatterjee")
                .build();
        GreetingServiceGrpc.GreetingServiceBlockingStub greetSycClient= GreetingServiceGrpc.newBlockingStub(channel);
        //Server Streaming
        GreetManyTimesRequest greetManyTimesRequest=GreetManyTimesRequest.newBuilder()
                .setGreeting(greeting)
                .build();
        greetSycClient.greetManyTimes(greetManyTimesRequest)
                .forEachRemaining(greetManyTimesResponse -> System.out.println(greetManyTimesResponse.getResult()));

    }

    private void doUnaryCall(ManagedChannel channel) {
        //Dummy Service
        //DummyServiceGrpc.DummyServiceBlockingStub syncClient=DummyServiceGrpc.newBlockingStub(channel);
        //System.out.println("Creating async stub");
        //DummyServiceGrpc.DummyServiceFutureStub asyncClient=DummyServiceGrpc.newFutureStub(channel);
        //Greeting Service

        Greeting greeting=Greeting.newBuilder()
                .setFirstName("Jishnu")
                .setLastName("Chatterjee")
                .build();
        GreetingServiceGrpc.GreetingServiceBlockingStub greetSycClient= GreetingServiceGrpc.newBlockingStub(channel);


        //Unary

        GreetingRequest greetingRequest=GreetingRequest.newBuilder()
                                        .setGreeting(greeting)
                                        .build();
        GreetingResponse greetingResponse = greetSycClient.greet(greetingRequest);

        System.out.println("Response-> "+greetingResponse.getResult());

    }


}
