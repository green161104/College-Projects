package ApplicationLogic.Demo.CPU;

import ApplicationLogic.Demo.Kernel.Kernel;
import ApplicationLogic.Demo.MessageTask.RunnableTaskClass;

import javax.print.attribute.standard.RequestingUserName;
import java.util.LinkedList;
import java.util.Queue;

/*
The ApplicationLogic.Demo.CPU is the brain of the satellite.
It executes instructions from the software applications and performs calculations,
data processing, and overall system control. In satellite communication,
the ApplicationLogic.Demo.CPU plays a crucial role in handling data received from Earth or other satellites,
processing that data, and directing the transmission of information back to the appropriate destination.
 */
public class CPU implements Runnable {


    private final Kernel kernel;
    private final Queue<RunnableTaskClass> taskQueue;
    private int timeQuantum; // Time quantum for Round Robin

    public CPU(Kernel kernel) {
        this.kernel = kernel;
        /*In this code, RunnableTaskClass.setCPU(this) shows that
         there's a class or method named RunnableTaskClass with a
         static method setCPU that takes a cpu object as an argument.
         This method is being called in the constructor of the cpu
         class to inform all tasks about the CPU.

         Understanding the Flow:

    - When an instance of the cpu class is created, the constructor is called.
    - The kernel field is initialized with the provided kernel parameter.
    - The TaskMessage.setCPU(this) method is called, passing the current cpu object (this) as an argument.
    - The setCPU method in the TaskMessage class stores a reference to the cpu object in the cpuReference variable.
         */
        this.taskQueue = new LinkedList<>();
        this.timeQuantum = 2;
        RunnableTaskClass.SetSatelliteCPU(this);
    }

    public Queue getTaskQueue() {
        return this.taskQueue;
    }

    @Override
    public void run() {
        while (kernel.getisOn() && !kernel.getIsShuttingDown()) {

            if (!kernel.getTasksToExecuteFromKernel().isEmpty()) {
                RunnableTaskClass taskForCycle = kernel.getTasksToExecuteFromKernel().remove(0);
                if (taskForCycle != null) {
                    taskQueue.add(taskForCycle);
                    System.out.println("fui adicionada ao buffer do cpu");
                }
            }

            // Execute tasks using Round Robin
            while (!taskQueue.isEmpty()) {
                RunnableTaskClass currentTask = taskQueue.remove();
                System.out.println("fui removida da taskQueue do CPU");

                // Check if shutting down after removing a task from the queue
                if (kernel.getIsShuttingDown()) {
                    break;
                }

                kernel.AllocateMemmory(currentTask);
                executeTask(currentTask);
                System.out.println("o CPU tentou correr a task");

                if (currentTask.getisDone()) {
                    kernel.DeallocateMemmory(currentTask);
                    System.out.println("dealoquei a memoria");
                    sendTaskToKernel(currentTask);
                    System.out.println("mandei para o kernel");
                    System.out.println(kernel.getTasksExecutedAndReadyToSendToMiddleware().size());
                    kernel.sendTaskToMiddleware(currentTask);
                } else {
                    taskQueue.add(currentTask);
                }

                // Check if the time quantum has been reached
                if (--timeQuantum == 0) {
                    break; // Move to the next task if the time quantum is reached
                }
            }

            // Reset the time quantum for the next cycle
            timeQuantum = 2;

            // Process remaining tasks before shutting down
            while (!taskQueue.isEmpty()) {
                RunnableTaskClass currentTask = taskQueue.remove();
                executeTask(currentTask);

                if (currentTask.getisDone()) {
                    sendTaskToKernel(currentTask);
                }
            }

            // Remove remaining tasks if the kernel wants to shut down
            if (kernel.getIsShuttingDown()) {
                taskQueue.clear();
            }
        }
    }
        //while the kernel is on and not wanting to shutdown, the cpu is supposed to get tasks
        //from the kernel and executing them, and then notify the kernel that the task is done executing.
        /*
        public void run() {

        while (kernel.getisOn() && !kernel.getIsShuttingDown()) { // enquanto kernel is on and not wanting to shutdown

            if (!kernel.getIsShuttingDown()) { // se o kernel n quiser fazer shutdown
                if (!kernel.getTasksToExecuteFromKernel().isEmpty()) { // se a tasksToExecute do kernel n estiver vazia
                    RunnableTaskClass taskForCycle = kernel.getTasksToExecuteFromKernel().remove(0); // remover a primeira task
                    if (taskForCycle != null) { // se ela n é nula
                        taskQueue.add(taskForCycle); //adicionar à queue do CPU
                        System.out.println("fui adicionada ao buffer do cpu"); //check para ver se foi adicionado
                    }
                }
                // Execute tasks using Round Robin
                while (!taskQueue.isEmpty()) { //se a queue do cpu n estiver vazia
                    RunnableTaskClass currentTask = taskQueue.remove(); //criar uma task e removar da queue do cpu
                    System.out.println("fui removida da taskQueue do CPU"); // mostrar que fez
                    kernel.AllocateMemmory(currentTask); // alocar memoria na RAM
                    executeTask(currentTask); // executar a task
                    System.out.println("o CPU tentou correr a task"); // verificaçao de que a task foi lançada pelo cpu

                    if (currentTask.getisDone()) { // se a task foi completa
                        kernel.DeallocateMemmory(currentTask); //dealoca a memoria do RAM
                        System.out.println("dealoquei a memoria"); //confirm q dealocou
                        sendTaskToKernel(currentTask); //manda a task para o kernel
                        System.out.println("mandei para o kernel"); //confirm que mandou
                        System.out.println(kernel.getTasksExecutedAndReadyToSendToMiddleware().size());  // print do kernel executed tasks
                        kernel.sendTaskToMiddleware(currentTask); // manda do kernel para o middleware.
                    } else { // se a task n foi completada
                        taskQueue.add(currentTask); //adicionada à queue novamente
                    }

                    // Check if the time quantum has been reached

                }

                timeQuantum = 2;
            } else {
                if (!kernel.getIsShuttingDown()) {
                }
            }
            while (!taskQueue.isEmpty()) {
                RunnableTaskClass taskToExecute = taskQueue.poll();
                kernel.AllocateMemmory(taskToExecute);
                executeTask(taskToExecute);

                if (taskToExecute.getisDone()) {
                    kernel.DeallocateMemmory(taskToExecute);
                    kernel.sendTaskToMiddleware(taskToExecute);
                }
            } // abruptly force tasks to be removed if the kernel wants to shut down

            while (!taskQueue.isEmpty()) {
                taskQueue.remove();
            }
        }

    }
        flow of the code
                if kernel is not shutting down and on
                        take task from the kernel's array of tasks
                        execute them for a certain time ( round robin )
                        are they done? if so, send them back up, if not send them back to waiting
                        check for new tasks and throw them if they exist.
                   if the kernel wants to shut down
                        finish tasks in the kernel that have been assigned to be executed,
                        but stop taking tasks from the middleware into the kernel.
                   after the tasks are done, stop executing the thread.
         */

    private void sendTaskToKernel(RunnableTaskClass taskToExecute) {
        kernel.getTasksExecutedAndReadyToSendToMiddleware().add(taskToExecute);
    }

    /*
    private RunnableTaskClass receiveTasksFromMiddleware() {
        RunnableTaskClass taskToBeExecutedByCPU = kernel.getTasksToExecuteFromKernel().removeFirst();
        return taskToBeExecutedByCPU;
    }*/

    public void receiveTaskFromKernel(RunnableTaskClass taskToBeExecutedByCPU) {
        /* workflow deste método
         * se o kernel n estiver shuttindown;
         * adiciona a task recebida à queue
         */
        if (!kernel.getIsShuttingDown()) {
            if (!taskQueue.contains(taskToBeExecutedByCPU)) {
                taskQueue.add(taskToBeExecutedByCPU);
                System.out.println("fui recebida pela Queue de tasks no cpu.");
            }
        }
    }

    private void executeTask(RunnableTaskClass currentTask) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        currentTask.run();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

}
