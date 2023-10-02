package gprc.example.service;

import gprc.example.stubs.Bank;
import gprc.example.stubs.BankServiceGrpc;
import io.grpc.stub.StreamObserver;



public class GrpcBankService  extends BankServiceGrpc.BankServiceImplBase {



    @Override
    public void convert(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String currencyForm= request.getCurrencyFrom();
        String currencyTo=request.getCurrencyTo();
        double amount= request.getAmount();
        //all    recovered  objects by protocol buffer are immutable, so use builder buffer
        Bank.ConvertCurrencyResponse response= Bank.ConvertCurrencyResponse.newBuilder()

                .setCurrencyFrom(currencyForm)
                .setCurrencyTo(currencyTo)
                .setAmount(amount)
                .setResult(amount*12.4)
                .build();
        //send reponse
        responseObserver.onNext(response);
        //inform client that is completed
        responseObserver.onCompleted();

    }

    @Override
    public void getCurrencyStream(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        super.getCurrencyStream(request, responseObserver);
    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> performstream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertCurrencyRequest>() {
            double sum = 0;

            @Override
            public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
                sum += convertCurrencyRequest.getAmount();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder()

                        .setResult(sum * 11.4)
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();

            }
        };

    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> fullCurrencystream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        return  new StreamObserver<Bank.ConvertCurrencyRequest>() {
            //each item is ready , send to client
            @Override
            public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
                Bank.ConvertCurrencyResponse response= Bank.ConvertCurrencyResponse.newBuilder()
                        .setResult(convertCurrencyRequest.getAmount()*Math.random()*40)
                        .build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();

            }
        };
    }




}
