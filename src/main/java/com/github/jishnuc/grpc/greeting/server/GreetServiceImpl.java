package com.github.jishnuc.grpc.greeting.server;

import com.github.jishnuc.proto.Greeting;
import com.github.jishnuc.proto.GreetingRequest;
import com.github.jishnuc.proto.GreetingResponse;
import com.github.jishnuc.proto.GreetingServiceGrpc;
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
}
