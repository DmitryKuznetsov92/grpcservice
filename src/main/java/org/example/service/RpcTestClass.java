package org.example.service;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.example.RpcServiceGrpc;
import org.example.TestRequest;
import org.example.TestResponse;

import java.util.Map;

@GrpcService
public class RpcTestClass extends RpcServiceGrpc.RpcServiceImplBase {

    private final Map<Integer, TestResponse> availableTest = Map.of(
            1, TestResponse.newBuilder()
                    .setId(1)
                    .setName("one")
                    .build(),
            2, TestResponse.newBuilder()
                    .setId(2)
                    .setName("two")
                    .build(),
            3, TestResponse.newBuilder()
                    .setId(3)
                    .setName("three")
                    .build()
    );

    @Override
    public void getTest(TestRequest request, StreamObserver<TestResponse> responseObserver) {
        TestResponse response = availableTest.get(request.getId());
        responseObserver.onNext(response.toBuilder().setDescr("same descr for all").build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllTest(Empty request, StreamObserver<TestResponse> responseObserver) {
        availableTest.values().stream()
                .peek(r -> sleep())
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
