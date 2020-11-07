package com.github.jishnuc.grpc.calculator.server;

import com.github.jishnuc.proto.CalculatorServiceGrpc;
import com.github.jishnuc.proto.SumRequest;
import com.github.jishnuc.proto.SumResponse;
import io.grpc.stub.StreamObserver;

public class CalculatorServiceImpl extends CalculatorServiceGrpc.CalculatorServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        //extract request
        int op1=request.getOp1();
        int op2=request.getOp2();

        //add to operands

        int result=op1+op2;
        //create response
        SumResponse response=SumResponse.newBuilder()
                                        .setResult(result)
                                        .build();

        //send response
        responseObserver.onNext(response);

        //complete
        responseObserver.onCompleted();
    }
}
