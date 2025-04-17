package org.qum.iotdataprocessingsystem.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import prediction.PredictionServiceGrpc;
import prediction.PredictionServiceOuterClass.*;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.health.ServiceHealth;

@Service
public class PredictionClient {
    private final ManagedChannel channel;
    private final PredictionServiceGrpc.PredictionServiceBlockingStub stub;

    public PredictionClient(ServiceDiscovery serviceDiscovery) {
//        ServiceDiscovery discovery = new ServiceDiscovery("localhost", 8500);
        ServiceHealth service = serviceDiscovery.getService("prediction-service");
        this.channel = ManagedChannelBuilder.forAddress(
                service.getService().getAddress(),
                service.getService().getPort()
        ).usePlaintext().build();
        this.stub = PredictionServiceGrpc.newBlockingStub(channel);
    }

    public int predict(double temperature, double pressure, double vibration, double humidity) {
        PredictionRequest request = PredictionRequest.newBuilder()
                .setTemperature(temperature)
                .setPressure(pressure)
                .setVibration(vibration)
                .setHumidity(humidity)
                .build();
        PredictionResponse response = stub.predict(request);
        return response.getFaulty();
    }

    @PreDestroy
    public void shutdown() {
        channel.shutdown();
    }

//    public static void main(String[] args) {
//        PredictionClient client = new PredictionClient();
//        int result = client.predict(65.5f, 41.3f, 2.8f, 60.2f);
//        System.out.println("Prediction result: " + result);
//        client.shutdown();
//    }
}
