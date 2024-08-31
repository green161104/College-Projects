package ApplicationLogic.Demo.MessageTask;

import ApplicationLogic.Demo.CPU.CPU;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RunnableTaskClass implements Runnable, Comparable<RunnableTaskClass>{

    //ONLY THE KERNEL KNOWS OF THE CPU's EXISTENCE.

    private Message messageInTask; // message to be changed in the run method.
    private boolean isDone;
    private TaskIdentifierFlags taskFunctionId; // flag that identifies the task for this task
    private double memmory; // memmory that this task owns
    private static CPU satelliteCPU; // cpu that all threads (tasks) share
    private int priority;

    /**
     * Constructor for the RunnableTaskClass, the flag identifies what a specific thread is supposed to do.
     * memmory identifies the amount of RAM a task occupies.
     *
     * @param messageInTask       message inside the thread;
     * @param functionForThisTask id for the task
     */
    public RunnableTaskClass(Message messageInTask, TaskIdentifierFlags functionForThisTask) {
        this.messageInTask = messageInTask;
        this.isDone = false;
        this.taskFunctionId = functionForThisTask;
        this.priority = 10;
        this.memmory = Math.floor(Math.random() * (50 - 1 + 1) + 1);
    }

    public boolean getisDone(){ return this.isDone; }
    public int getpriority(){return this.priority;}
    public static void SetSatelliteCPU(CPU cpu) {
        satelliteCPU = cpu;
    } //sets the shared cpu for all threads

    public TaskIdentifierFlags GetFunctionForThisTask() {
        return this.taskFunctionId;
    } // getter for taskIDs

    public void SetFunctionForThisTask(TaskIdentifierFlags functionForThisTask) {
        this.taskFunctionId = functionForThisTask;
    } //setter for changing taskIds

    public double Getmemmory() {
        return this.memmory;
    } //getter memmory value

    public Message GetMessageInTask() {
        return messageInTask;
    } //Getter for messages in tasks

    public void SetMessageInTask(Message messageInTask) {
        this.messageInTask = messageInTask;
    } //setter for messages in tasks

    /**
     * this method changes every character in a message's content to +2 value
     */
    private void encode() {
        if (messageInTask.getContent() != null) {
            char[] contentChars = messageInTask.getContent().toCharArray();
            for (int i = 0; i < contentChars.length; i++) {
                contentChars[i] += 2; // Shift each character by 2 (simple encoding)
            }
            messageInTask.setContent(new String(contentChars)); // Update the content in the original Message object
            messageInTask.setTitle("secret message : ");
        } else {System.out.println("there's no message to encode :(");}
    }

        /**
         * this method changes every character in a message's content to -2 value
         */
        private void decode(){
            if (messageInTask.getContent() != null) {
                char[] contentChars = messageInTask.getContent().toCharArray();
                for (int i = 0; i < contentChars.length; i++) {
                    contentChars[i] -= 2; // Shift each character by 2 (simple encoding)
                }
                messageInTask.setContent(new String(contentChars)); // Update the content in the original Message object

                String result = "Encoded Message: " + messageInTask.getContent();
            } else {System.out.println("No message set for encoding.");}
        }

        /**
         * this method creates a location report and sets it as the content for the message of this task
         */
        private void locationCreation() {
            //  latitude and longitude are randomly generated for each task
            double latitude = Math.random() * 90;  // Random latitude between -90 and 90
            double longitude = Math.random() * 180; // Random longitude between -180 and 180
            String coordinates = new String("Current Location -> Latitude: " + latitude + ", Longitude : " + longitude + ".");
            this.messageInTask.setContent(coordinates);
            System.out.println(coordinates);
        }

        /**
         * this method prints out a report for the satellite.
         * it'll present satelliteName, batteryLevel, SignalStrength, and if it's operational or not
         */
        private void satelliteReport() {
            String satelliteName = "Satellite-SO101";
            int batteryLevel = (int) (Math.floor(Math.random() * (100 - 1  + 1) + 1 )); // Random battery level between 0 and 100
            int signalStrength = (int) (Math.floor(Math.random() * (100 - 50  + 1) + 50 )); // Random signal strength between 0 and 100
            String isOperational = new String(" Satellite is operational. "); // Randomly set as operational or not
            // Format the satellite report
            String report = String.format("Satellite Report:\nName: %s\nBattery Level: %d%%\nSignal Strength: %d%%\nOperational: %s",
                    satelliteName, batteryLevel, signalStrength, isOperational);
            // Print the report
            System.out.println(report);
        }

    /**
     *
     */
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            StringBuilder result = new StringBuilder();

            switch (taskFunctionId) {
                case ENCODE:
                    System.out.println("encoding the " + messageInTask.getTitle() + " message");
                    result.append("encoding the ").append(messageInTask.getTitle()).append(" message").append(System.lineSeparator());
                    encode();
                    result.append(messageInTask.getContent());
                    result.append("[").append(messageInTask.getTitle()).append("] has been encoded to : ").append(messageInTask.getContent()).append(System.lineSeparator());
                    //satellitecpu.taskDoneExecuting(this); // task done executing so goes up to cpu
                    break;
                case DECODE:
                    System.out.println("encoding the " + messageInTask.getTitle() + " message");
                    result.append("decoding the ").append(messageInTask.getTitle()).append(" message").append(System.lineSeparator());
                    decode();
                    result.append(messageInTask.getContent());
                    result.append("[").append(messageInTask.getTitle()).append("] has been decoded to : ").append(messageInTask.getContent()).append(System.lineSeparator());
                    System.out.println("[" + messageInTask.getTitle() + "] has been encoded to : " + messageInTask.getContent());
                    //satellitecpu.taskDoneExecuting(this); // task done executing so goes up to cpu
                    break;
                case REPORT:
                    System.out.println("About to receive Satellite Report");
                    result.append("About to receive Satellite Report").append(System.lineSeparator());
                    satelliteReport();
                    result.append(messageInTask.getContent());
                    result.append("End of satellite report").append(System.lineSeparator());
                    System.out.println("End of satellite report");
                    //satellitecpu.taskDoneExecuting(this);
                    break;
                case LOCATION:
                    System.out.println("About to print out satellite Location");
                    result.append("About to print out satellite Location").append(System.lineSeparator());
                    locationCreation();
                    result.append(messageInTask.getContent());
                    result.append("End of location report.").append(System.lineSeparator());
                    System.out.println("End of location report.");
                    //satellitecpu.taskDoneExecuting(this);
                    break;
                default:
                    System.out.println("this task doesn't have an identifier associated");
                    break;
            }

            this.isDone = true;

            // Write the result to a file
            writeResultToFile(result.toString(), "Responses.txt");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }



    private void writeResultToFile(String result, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("user.home") + "/Desktop/" + fileName))) {
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(RunnableTaskClass o) {
        if (priority == (o.priority)) {
            return 0; // Priorities are equal
        } else {
            return Integer.compare(priority, o.priority);
        }
    }



    public void decreasePriority() {
        this.priority--;
    }

}

