package com.github.jishnuc.grpc.calculator.server;

import com.github.jishnuc.proto.*;
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

    @Override
    public void primeNumberDecompose(PrimeNumberDecomposeRequest request, StreamObserver<PrimeNumberDecomposeResponse> responseObserver) {
        //extract request
        int num=request.getNumber();
        int k=2;
        try{
            while(num>1){
                if(num%k==0){
                    PrimeNumberDecomposeResponse response=PrimeNumberDecomposeResponse.newBuilder()
                            .setPrimeFactor(k)
                            .build();
                    responseObserver.onNext(response);
                    num/=k;
                    Thread.sleep(1000L);
                }else {
                    k+=1;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            responseObserver.onCompleted();
        }

    }
}
