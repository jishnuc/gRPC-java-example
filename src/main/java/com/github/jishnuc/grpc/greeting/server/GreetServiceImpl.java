package com.github.jishnuc.grpc.greeting.server;

import com.github.jishnuc.proto.*;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        //extract fields from request
        Greeting greeting=request.getGreeting();
        String firstName=greeting.getFirstName();

        //create response
        String result="Hello "+firstName;
        GreetingResponse response=GreetingResponse.newBuilder()
                                    .setResult(result)
                                    .build();
        //send response
        responseObserver.onNext(response);
        //complete RPC call
        responseObserver.onCompleted();

    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        //extract fields from request
        Greeting greeting=request.getGreeting();
        String firstName=greeting.getFirstName();
        try {
            for(int i=0;i<10;i++){
                String result="Hello "+firstName+", response #"+i;
                GreetManyTimesResponse response=GreetManyTimesResponse.newBuilder()
                                                .setResult(result)
                                                .build();
                responseObserver.onNext(response);

                Thread.sleep(1000L);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }

    }
}
