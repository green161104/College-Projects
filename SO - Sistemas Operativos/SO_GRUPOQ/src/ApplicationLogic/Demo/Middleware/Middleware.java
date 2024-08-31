package ApplicationLogic.Demo.Middleware;
    /*
    ApplicationLogic.Demo.Middleware serves as a bridge between different software applications and the kernel.
    It provides a set of services and functionalities that allow applications to communicate
    with each other and with the underlying hardware without needing to understand the
    complexities of the hardware or the specific details of other applications.
    In satellite communication, middleware can help manage data transmission protocols,
    error handling, and the integration of various software components
    involved in the communication process.
     */

import ApplicationLogic.Demo.Kernel.Kernel;
import ApplicationLogic.Demo.MessageTask.RunnableTaskClass;
import Util.singlyLinkedLists.SinglyLinkedOrderedList;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Middleware {
    private Kernel kernel;
    private SinglyLinkedOrderedList<RunnableTaskClass> bufferInMiddleware;
    private Semaphore semaphoreForSendingTasks;
    private Queue<RunnableTaskClass> middlewareRegistryOfCompletedTasks;

    public Semaphore GetSemaphoreForSendingTasks() {
        return this.semaphoreForSendingTasks;
    }

    public SinglyLinkedOrderedList GetsinglyLinkedOrderedList() {
        return this.bufferInMiddleware;
    }

    public Queue getmiddleWareRegistryOfCompletedTasks() {
        return this.middlewareRegistryOfCompletedTasks;
    }

    public Kernel getKernel() {
        return this.kernel;
    }

    public Middleware() {
        this.kernel = new Kernel(this);
        this.bufferInMiddleware = new SinglyLinkedOrderedList<>();
        this.semaphoreForSendingTasks = new Semaphore(5); // size 5 para o bufferInMiddleware
        this.middlewareRegistryOfCompletedTasks = new LinkedBlockingQueue<>();
    }

    public synchronized void sendJobToMiddleware(RunnableTaskClass taskToSend) {
      /*workflow do método
      * acquire semaforo para só poder enviar 5 tasks no max
      * decrementa a priority de todas as tasks
      * adiciona a task ao buffer no middleware (SinglyLinkedOrderedList)   - importante
      * remove a task do buffer no middleware
      * chama o kernel e o método receiveTaskFromMiddleware - importante para receber
      * */

        try {
            semaphoreForSendingTasks.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } //obtain one of the 5 buffer spaces

        //System.out.println("The task is being processed");

        synchronized (bufferInMiddleware) {
            for (RunnableTaskClass task : bufferInMiddleware) {
                task.decreasePriority();
            }
            bufferInMiddleware.add(taskToSend); // a task é adicionada ao array de tasks agendadas no middleware
           // System.out.println("fui adicionada ao buffer do middleware"); // check if it was added
        }

        RunnableTaskClass taskToSendToKernel;
        synchronized (bufferInMiddleware) {
            taskToSendToKernel = bufferInMiddleware.removeFirst();
           // System.out.println("fui removida do buffer do middleware"); // check if removed
        }
        kernel.receiveTaskFromMiddleware(taskToSend);
    }

    public synchronized void receiveTaskInfoAfterCompletion() {

        RunnableTaskClass answeredTask = kernel.sendTaskToMiddleware;

        synchronized (middlewareRegistryOfCompletedTasks) {
            if (answeredTask != null) {
                middlewareRegistryOfCompletedTasks.add(answeredTask);
            }
        }
        semaphoreForSendingTasks.release(); //testar com isto aqui ou antes do synchronized block
        //System.out.println("dei release ao semáforo");
    }

    public void turnOnOperatingSystem() {
        System.out.println("buenas tardes :)");
        kernel.turnOn();
        System.out.println("casa abierta");
    }

    synchronized public void turnOffOperatingSystem() {
        System.out.println("buenas noches :(");
        kernel.shutdown();
        System.out.println("Estoy cansado...");
    }
}
