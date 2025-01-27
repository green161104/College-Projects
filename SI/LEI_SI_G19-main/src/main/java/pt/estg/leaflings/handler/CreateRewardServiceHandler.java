package pt.estg.leaflings.handler;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.client.api.worker.JobHandler;
import pt.estg.leaflings.models.CreateReward;

import java.util.HashMap;
import java.util.Map;

public class CreateRewardServiceHandler implements JobHandler {

    CreateReward reward = new CreateReward();

    @Override
    public void handle(JobClient client, ActivatedJob job) {
        try {

            client.newCompleteCommand(job.getKey())
                    .variables("{\"adjustmentsNeeded\": true}")
                    .send()
                    .join();

            reward.createReward();
        } catch (Exception e) {
            e.printStackTrace();
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .send()
                    .join();
        }
    }
}

