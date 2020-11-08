package com.github.jishnuc.grpc.greeting.client;

import com.github.jishnuc.proto.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("GrPC client");

        ManagedChannel channel= ManagedChannelBuilder.forAddress("localhost",50051)
                                .usePlaintext()
                                .build();
        System.out.println("Creating blocking stub");
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
       /* 
        GreetingRequest greetingRequest=GreetingRequest.newBuilder()
                                        .setGreeting(greeting)
                                        .build();
        GreetingResponse greetingResponse = greetSycClient.greet(greetingRequest);

        System.out.println("Response-> "+greetingResponse.getResult());*/

        
        //Server Streaming
        GreetManyTimesRequest greetManyTimesRequest=GreetManyTimesRequest.newBuilder()
                                                    .setGreeting(greeting)
                                                    .build();
        greetSycClient.greetManyTimes(greetManyTimesRequest)
                        .forEachRemaining(greetManyTimesResponse -> System.out.println(greetManyTimesResponse.getResult()));

        System.out.println("Shutdown");
        channel.shutdown();
    }
}
