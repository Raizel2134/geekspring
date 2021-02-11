package com.geekbrains.geekspring.repositories;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class GrpcProvider {

    public ManagedChannel getConnection() {
       return ManagedChannelBuilder.forAddress("localhost", 9080)
                .usePlaintext()
                .build();
    }

}
