package pt.estg.leaflings;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.worker.JobWorker;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProvider;
import io.camunda.zeebe.client.impl.oauth.OAuthCredentialsProviderBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.estg.leaflings.handler.CreatePlantServiceHandler;

import java.time.Duration;

@SpringBootApplication
public class CreateNewPlant {
    private static final String ZEEBE_ADDRESS = "dfdb8d36-5bf6-4b20-be42-8205ce0805f0.bru-2.zeebe.camunda.io:443";
    private static final String ZEEBE_CLIENT_ID = "2nB6jihosaqDFM~~k21libEkbZoSVma0";
    private static final String ZEEBE_CLIENT_SECRET = "Upkf71~OEfacizRkKi2lj1GIOx8cyUdpgWslzd5_HJSTmtRJOf5Knpwi1.V4AP~A";
    private static final String ZEEBE_AUTHORIZATION_SERVER_URL = "https://login.cloud.camunda.io/oauth/token";
    private static final String ZEEBE_TOKEN_AUDIENCE = "zeebe.camunda.io";

    public static void main(String[] args) {
        final OAuthCredentialsProvider credentialsProvider =
                new OAuthCredentialsProviderBuilder()
                        .authorizationServerUrl(ZEEBE_AUTHORIZATION_SERVER_URL)
                        .audience(ZEEBE_TOKEN_AUDIENCE)
                        .clientId(ZEEBE_CLIENT_ID)
                        .clientSecret(ZEEBE_CLIENT_SECRET)
                        .build();

        try (final ZeebeClient client =
                     ZeebeClient.newClientBuilder()
                             .gatewayAddress(ZEEBE_ADDRESS)
                             .credentialsProvider(credentialsProvider)
                             .build()) {

            client.newCreateInstanceCommand()
                    .bpmnProcessId("Process_1sbgcy4")
                    .latestVersion()
                    .variables("{\"plantDataId\": \"12345\"}")
                    .send()
                    .join();

            client.newPublishMessageCommand()
                    .messageName("PlantDataReceived")
                    .correlationKey("12345")
                    .variables("{\"additionalInfo\": \"Data received successfully\"}")
                    .send()
                    .join();


            final JobWorker createNewPlant =
                    client.newWorker()
                            .jobType("NewPlant")
                            .handler(new CreatePlantServiceHandler())  // Implementação do handler
                            .timeout(Duration.ofSeconds(10).toMillis())
                            .open();

            Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }

        SpringApplication.run(CreateNewPlant.class, args);
    }
}
