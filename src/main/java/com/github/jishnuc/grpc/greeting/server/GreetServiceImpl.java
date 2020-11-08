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

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        StreamObserver<LongGreetRequest> requestStreamObserver=new StreamObserver<LongGreetRequest>() {
            String result="";
            @Override
            public void onNext(LongGreetRequest value) {
                //Client sends message
                result+="Hello "+value.getGreeting().getFirstName()+"! ";
            }

            @Override
            public void onError(Throwable t) {
                //Client sends error
            }

            @Override
            public void onCompleted() {
                //Client is done
                responseObserver.onNext(LongGreetResponse.newBuilder().setResult(result).build());
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        StreamObserver<GreetEveryoneRequest> requestStreamObserver=new StreamObserver<GreetEveryoneRequest>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                String response="Hello "+value.getGreeting().getFirstName()+"! ";
                GreetEveryoneResponse greetEveryoneResponse=GreetEveryoneResponse.newBuilder()
                                                                                .setResult(response)
                                                                                .build();
                responseObserver.onNext(greetEveryoneResponse);

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
        return requestStreamObserver;

    }
}
