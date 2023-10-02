package gprc.example.server;

import gprc.example.service.GrpcBankService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Grpcserver {
    public static void main(String[] args) throws InterruptedException, IOException {

        Server server= ServerBuilder.forPort(5555)
                .addService(new GrpcBankService())
                .build();
        server.start();
        server.awaitTermination();
    }
}
