import ApplicationLogic.Demo.CPU.CPU;
import ApplicationLogic.Demo.Kernel.Kernel;
import ApplicationLogic.Demo.MEM.MEM;
import ApplicationLogic.Demo.MessageTask.Message;
import ApplicationLogic.Demo.MessageTask.RunnableTaskClass;
import ApplicationLogic.Demo.MessageTask.TaskIdentifierFlags;
import ApplicationLogic.Demo.Middleware.Middleware;

import java.util.Scanner;

public class Main {
    private static Middleware middleware = new Middleware();

    public static void main(String[] args) {
        MEM memory = new MEM();
        Kernel kernel = new Kernel(middleware);
        CPU cpu = new CPU(kernel);

        // Initialize the Middleware with the Kernel
        middleware.turnOnOperatingSystem();

        Scanner scanner = new Scanner(System.in);

        // Simple menu for the user
        while (true) {

            try {
                // Assuming that the sleep is intended to simulate some time passing
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println("Choose an option:");
            System.out.println("1. Create Location Task");
            System.out.println("2. Create Encode Task");
            System.out.println("3. Create Report Task");
            System.out.println("4. Simulate stress test");
            System.out.println("5. exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    createAndSendTask(middleware, TaskIdentifierFlags.LOCATION);
                    break;
                case 2:
                    createAndSendTask(middleware, TaskIdentifierFlags.ENCODE);
                    break;
                case 3:
                    createAndSendTask(middleware, TaskIdentifierFlags.REPORT);
                    break;
                case 4:
                    simulateStressTest();
                    try {
                        // Assuming that the sleep is intended to simulate some time passing
                        Thread.sleep(18000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    // Exit the loop and close the application

                    middleware.turnOffOperatingSystem();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }


        }
    }

    private static void createAndSendTask(Middleware middleware, TaskIdentifierFlags taskType) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter task title:");
        String title = scanner.nextLine();

        System.out.println("Enter task content:");
        String content = scanner.nextLine();

        Message message = new Message(title, content);
        RunnableTaskClass task = new RunnableTaskClass(message, taskType);
        middleware.sendJobToMiddleware(task);
        System.out.println("Task created and sent to Middleware.");
        try {
            //simula processing
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        middleware.receiveTaskInfoAfterCompletion();
    }

    private static void simulateStressTest(){
        for(int i = 0; i < 10; i++) {
            Message message = new Message("title", "content");
            RunnableTaskClass task = new RunnableTaskClass(message, TaskIdentifierFlags.REPORT);
            middleware.sendJobToMiddleware(task);

            System.out.println("Task created and sent to Middleware.");
            try {
                //simula processing
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            middleware.receiveTaskInfoAfterCompletion();
        }
    }
}