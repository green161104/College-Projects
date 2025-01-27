package pt.estg.leaflings.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import pt.estg.leaflings.models.CreatePlant;

import java.util.HashMap;
import java.util.Map;

public class CreatePlantServiceHandler implements JobHandler {

    CreatePlant plant = new CreatePlant();

    @Override
    public void handle(JobClient client, ActivatedJob job) {
        System.out.println("Handling job: " + job);
        System.out.println("Received variables: " + job.getVariables());

        try {
            // Extract existing variables from the job
            Map<String, Object> variables = job.getVariablesAsMap();

            // Ensure necessary variables are present
            if (!variables.containsKey("plantDataId") || variables.get("plantDataId") == null) {
                throw new IllegalArgumentException("Missing required variable: plantDataId");
            }

            // Retrieve plantDataId and log for debugging
            String plantDataId = (String) variables.get("plantDataId");
            System.out.println("Plant Data ID: " + plantDataId);

            // Perform the plant creation operation
            plant.create();
            System.out.println("Plant creation completed successfully");

            // Add or update variables to reflect completion
            Map<String, Object> updatedVariables = new HashMap<>(variables);
            updatedVariables.put("isDataComplete", true);
            updatedVariables.put("isAccordingToStandards", true);
            updatedVariables.put("status", "Plant data processed successfully");

            // Complete the job and return updated variables
            client.newCompleteCommand(job.getKey())
                    .variables(updatedVariables)
                    .send()
                    .join();

            System.out.println("Job completed successfully with updated variables: " + updatedVariables);

        } catch (Exception e) {
            // Log the error and fail the job with reduced retries
            e.printStackTrace();
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage("Error processing the CreatePlantService job: " + e.getMessage())
                    .send()
                    .join();
        }
    }
}
