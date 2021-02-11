package com.geekbrains.geekspring.repositories;

import com.baeldung.grpc.RolesRequest;
import com.baeldung.grpc.RolesResponse;
import com.baeldung.grpc.RolesServiceGrpc;
import com.geekbrains.geekspring.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleGrpcRepository {

    private GrpcProvider provider;

    public RoleGrpcRepository(@Autowired GrpcProvider provider) {
        this.provider = provider;
    }

    public Role findOneByName(String theRoleName) {
        RolesRequest request = buildMsg(theRoleName);
        RolesServiceGrpc.RolesServiceBlockingStub stub = RolesServiceGrpc.newBlockingStub(provider.getConnection());
        RolesResponse response = stub.getRoles(request);
        return new Role(Long.valueOf(response.getId()), response.getName());
    }

    private RolesRequest buildMsg(String roleName) {
        return RolesRequest.newBuilder()
                .setName(roleName)
                .build();
    }
}
