package ApplicationLogic.Demo.Kernel;

import ApplicationLogic.Demo.CPU.CPU;
import ApplicationLogic.Demo.MEM.MEM;
import ApplicationLogic.Demo.MessageTask.RunnableTaskClass;
import ApplicationLogic.Demo.Middleware.Middleware;
import Util.singlyLinkedLists.SinglyLinkedList;
import Util.singlyLinkedLists.SinglyLinkedOrderedList;

import java.util.LinkedList;
import java.util.List;

/*
The kernel is the core of the satellite's operating system.
It manages the system's resources (like ApplicationLogic.Demo.CPU, memory, and peripheral devices)
and facilitates communication between hardware and software components.
It handles tasks such as process management, memory management, and device control,
ensuring that different software applications can run effectively on the satellite.
 */
public class Kernel {

    private final Middleware middleware;
    public RunnableTaskClass sendTaskToMiddleware;
    private MEM RAM_Controlled_By_Kernel;
    private List<RunnableTaskClass> tasksToExecute;
    private SinglyLinkedOrderedList<RunnableTaskClass> tasksExecutedAndReadyToSendToMiddleware;
    private volatile  boolean isOn;
    private volatile  boolean isShuttingDown;
    private CPU CPU_Controlled_By_Kernel;
    Thread cpuThread;

    public Thread getCpuThread() {
        return this.cpuThread;
    }


    public SinglyLinkedOrderedList<RunnableTaskClass> getTasksExecutedAndReadyToSendToMiddleware() {
        return this.tasksExecutedAndReadyToSendToMiddleware;
    }

    public CPU getCPU_Controlled_By_Kernel() {
        return this.CPU_Controlled_By_Kernel;
    }

    public MEM getRAM_Controlled_By_Kernel() {
        return this.RAM_Controlled_By_Kernel;
    }

    public List<RunnableTaskClass> getTasksToExecuteFromKernel() {
        return this.tasksToExecute;
    }

    public Kernel(Middleware middleware) {
        this.middleware = middleware;
        this.tasksToExecute = new LinkedList<>();
        this.tasksExecutedAndReadyToSendToMiddleware = new SinglyLinkedOrderedList<>();
        this.cpuThread = new Thread(new CPU(this));
        this.isOn = false;
        this.isShuttingDown = false;
    }

    public void setOn(boolean value) {
        this.isOn = value;
    }

    public boolean getisOn() {
        return this.isOn;
    }

    public void setShuttingDown(boolean value) {
        this.isShuttingDown = value;
    }

    public boolean getIsShuttingDown() {
        return this.isShuttingDown;
    }

    public synchronized void AllocateMemmory(RunnableTaskClass task) {
        if (RAM_Controlled_By_Kernel.getUsedRAM() + (int) task.Getmemmory() > 200) {
            try {
                System.out.println("Not enough memory, waiting...");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted while waiting for memory", e);
            }
        }
        this.RAM_Controlled_By_Kernel.alocateMemmory((int) task.Getmemmory());
    }

    public synchronized void DeallocateMemmory(RunnableTaskClass task) {
        this.RAM_Controlled_By_Kernel.deallocateMemmory((int) task.Getmemmory());
        notifyAll();
    }

    public void receiveTaskFromMiddleware(RunnableTaskClass taskToSend) {
        /* workflow deste método
         * se o kernel n estiver shuttindown;
         * adiciona a task recebida à queue
         */
        if (!getIsShuttingDown()) {
            tasksToExecute.add(taskToSend);
            System.out.println("Fui recebida do middleware e estou no kernel agora");
        } else {
            System.out.println("Kernel is shutting down, not accepting tasks from middleware.");
        }
    }

    public synchronized void sendTaskToMiddleware(RunnableTaskClass task) {
        /* Recebe task do cpu, e envia para o middleware tasksDoneExecuting buffer
         *  */
        /*workflow do método
         * se a tasksToExecute tiver a task, remover pq já foi executada e só é suposto ser executada uma vez; // sim
         * remove a primeira tasks no tasksToExecute; // sim
         * se a task n for nula ENVIAR PARA O CPU // sim
         * remove a task do buffer kernel(?) - falta; // sim
         * chama o cpu e o método receiveTaskFromKernel - DONE: criar receiveTaskFromKernel
         * */

        if(tasksToExecute.contains(task)){tasksToExecute.remove(task);}
        RunnableTaskClass taskToReturnToCPU = tasksExecutedAndReadyToSendToMiddleware.removeFirst();
        middleware.getmiddleWareRegistryOfCompletedTasks().add(task);
        System.out.println("Task sent to middleware for further processing.");
    }

    public void turnOn() {
        this.RAM_Controlled_By_Kernel = new MEM();
        this.CPU_Controlled_By_Kernel = new CPU(this);
        Thread cpuThread = new Thread(this.CPU_Controlled_By_Kernel);
        cpuThread.start();
        this.isOn = true;
    }

    synchronized public void shutdown() {

        synchronized (this) {
            this.isShuttingDown = true;
            System.out.println("[Kernel] is on shutting down: " + isShuttingDown);
        }

        while (!tasksToExecute.isEmpty()) {
                System.out.println("Remaining tasks: " + tasksToExecute.size());
                tasksToExecute.clear();
        }

        /* o array de tasks a executar continua a executar, porém, não tiro mais do middleware. portanto
         *  if shuttingdown = true, then kernel doesn't take any more tasks from the middleware, but still
         * finishes the ones it has on the waitingTasks queue*/
        synchronized (this) {
            // Indicate that the shutdown process is complete.
            this.isOn = false;
            this.isShuttingDown = false;
            System.out.println("[Kernel] is on shutting down: " + isShuttingDown);
            cpuThread.interrupt();
        }
    }

    synchronized public void sendTaskToCPU() {
        /*workflow do método
         * se a tasksToExecute n está vazia; // sim
         * remove a primeira tasks no tasksToExecute; // sim
         * se a task n for nula ENVIAR PARA O CPU // sim
         * remove a task do buffer kernel(?) - falta; // sim
         * chama o cpu e o método receiveTaskFromKernel - DONE: criar receiveTaskFromKernel
         * */
        if (!tasksToExecute.isEmpty()) {
            RunnableTaskClass taskToReturnToCPU = tasksToExecute.remove(0);
            if (taskToReturnToCPU != null) {
                System.out.println("Fui removida do buffer de tasksToExecute do kernel");
                this.CPU_Controlled_By_Kernel.receiveTaskFromKernel(taskToReturnToCPU);
            }
        }
    }
}
