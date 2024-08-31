/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Project interface, as was provided to us.
 * It implements all the methods in the interface, which pertain to the management of an instance
 * of a project, and subsequent array of tasks. Adicionally, a method to check whether a participant
 * existed within a project was also added.
 */
package cblElements;

import cblElements.exceptions.NoTasksException;
import ma02_resources.participants.*;
import ma02_resources.project.Task;
import ma02_resources.project.exceptions.IllegalNumberOfParticipantType;
import ma02_resources.project.exceptions.IllegalNumberOfTasks;
import ma02_resources.project.exceptions.ParticipantAlreadyInProject;
import ma02_resources.project.exceptions.TaskAlreadyInProject;

public class MyProject implements ma02_resources.project.Project {

    private int maximum_tasks;
    private int maximum_participants;
    private int maximum_students;
    private int maximum_partners;
    private int maximum_facilitators;
    private String name;
    private String description;
    private int numberOfParticipants;
    private int numberOfStudents;
    private int numberOfPartners;
    private int numberOfFacilitators;
    private int numberOfTasks;
    private String[] tags;
    private Task[] tasks;
    private Participant[] participants;

    /**
     * Constructor for the project, which receives data for its struture through
     * the use of the template. As requested, throws an IllegalArgumentException
     * if the name, descriptionn or tags are null, or if the maximum number of
     * tasks or participants is negative or zero
     *
     * @param name            name for the project
     * @param description     a description of what the project entails
     * @param tags            tags that categorize the project
     * @param maxTasks        maximum of tasks per project, as defined in the template
     * @param maxPart         maximum participants in the project, as defined in the
     *                        template
     * @param maxStudents     maximum students in the project, as defined in the
     *                        template
     * @param maxFacilitators maximum facilitators in the project, as defined in
     *                        the template
     * @param maxPartners     maximum partners in the project, as defined in the
     *                        template
     */
    public MyProject(String name, String description, String[] tags, int maxTasks, int maxPart, int maxStudents, int maxFacilitators, int maxPartners) {
        try {
            this.name = name;
            this.description = description;
            this.tags = tags;
            this.numberOfTasks = 0;
            this.maximum_facilitators = maxFacilitators;
            this.maximum_participants = maxPart;
            this.maximum_partners = maxPartners;
            this.maximum_students = maxStudents;
            this.maximum_tasks = maxTasks;

            this.tasks = new Task[maximum_tasks];
            this.participants = new Participant[maximum_participants];
            this.numberOfFacilitators = 0;
            this.numberOfParticipants = 0;
            this.numberOfStudents = 0;
            this.numberOfPartners = 0;

            if (this.name.isEmpty() || this.description.isEmpty() || this.tags.length == 0 || 0 >= this.maximum_tasks || 0 >= this.maximum_participants) {
                throw new IllegalArgumentException("Arguments null. Please fill out the information accordingly.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Getter for the name of the project
     *
     * @return the project's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the project description
     *
     * @return the project's description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for the number of participants
     *
     * @return the number of participants currently in the project
     */
    @Override
    public int getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    /**
     * Getter for the number of students
     *
     * @return the number of students currently in the project
     */
    @Override
    public int getNumberOfStudents() {
        return this.numberOfStudents;
    }

    /**
     * Getter for the number of partners
     *
     * @return the number of partners currently in the project
     */
    @Override
    public int getNumberOfPartners() {
        return this.numberOfPartners;
    }

    /**
     * getter for the number of facilitators
     *
     * @return the number of facilitators currently in the project
     */
    @Override
    public int getNumberOfFacilitators() {
        return this.numberOfFacilitators;
    }

    /**
     * getter for the number of tasks
     *
     * @return the number of tasks currently in the project
     */
    @Override
    public int getNumberOfTasks() {
        return this.numberOfTasks;
    }

    /**
     * getter for the maximum tasks allowed in the project, as per the template
     *
     * @return the maximum tasks allowed in the project
     */
    @Override
    public int getMaximumNumberOfTasks() {
        return this.maximum_tasks;
    }

    /**
     * getter for the maximum participants allowed in the project, as per the
     * template
     *
     * @return the maximum participants allowed in the project
     */
    @Override
    public long getMaximumNumberOfParticipants() {
        return this.maximum_participants;
    }

    /**
     * getter for the maximum students allowed in the project, as per the
     * template
     *
     * @return the maximum students allowed in the project
     */
    @Override
    public int getMaximumNumberOfStudents() {
        return this.maximum_students;
    }

    /**
     * getter for the maximum students allowed in the project, as per the
     * template
     *
     * @return the maximum students allowed in the project
     */
    @Override
    public int getMaximumNumberOfPartners() {
        return this.maximum_partners;
    }

    /**
     * getter for the maximum students allowed in the project, as per the
     * template
     *
     * @return the maximum students allowed in the project
     */
    @Override
    public int getMaximumNumberOfFacilitators() {
        return this.maximum_facilitators;
    }

    /**
     * getter for the array of participants in the project
     *
     * @return the array of participants in this project
     */
    public Participant[] getParticipants() {
        return this.participants;
    }

    /**
     * Adds a participant to the array, and increments the number of
     * participants of said type for the instance
     *
     * @param participant participant to be added
     * @throws IllegalNumberOfParticipantType if the number of participants is
     *                                        at max capacity
     * @throws ParticipantAlreadyInProject    if the participant is already signed
     *                                        up for the project
     */
    @Override
    public void addParticipant(Participant participant) throws IllegalNumberOfParticipantType, ParticipantAlreadyInProject {
        Contact contact = participant.getContact();

        for (int i = 0; i < numberOfParticipants; i++) {
            if (participants[i] != null && participants[i].getContact().equals(contact)) {
                throw new ParticipantAlreadyInProject("Participant already exists.");
            }
        }

        if (Student.class.isAssignableFrom(participant.getClass())) {
            if (numberOfStudents >= maximum_students) {
                throw new IllegalNumberOfParticipantType("Too many Students.");
            }
            numberOfStudents++;
        } else if (Partner.class.isAssignableFrom(participant.getClass())) {
            if (numberOfPartners >= maximum_partners) {
                throw new IllegalNumberOfParticipantType("Too many Partners.");
            }
            numberOfPartners++;
        } else if (Facilitator.class.isAssignableFrom(participant.getClass())) {
            if (numberOfFacilitators >= maximum_facilitators) {
                throw new IllegalNumberOfParticipantType("Too many Facilitators.");
            }
            numberOfFacilitators++;
        }

        if (numberOfParticipants >= maximum_participants) {
            throw new IllegalNumberOfParticipantType("Too many Participants.");
        }

        participants[numberOfParticipants] = participant;
        numberOfParticipants++;

    }

    /**
     * removes a certain participant from the array, and decrements the counter
     * for the corresponding instance variable
     *
     * @param email participant's email
     * @return the removed participant, or null if it didn't succeed
     */
    @Override
    public Participant removeParticipant(String email) {
        try {
            for (int i = 0; i < numberOfParticipants; i++) {
                if (participants[i].getEmail().equals(email)) {
                    Participant removedParticipant = participants[i];
                    if (removedParticipant == null) {
                        throw new IllegalArgumentException("Participant doesn't exist");
                    }
                    for (int j = i; j < numberOfParticipants - 1; j++) {
                        participants[j] = participants[j + 1];
                    }

                    participants[numberOfParticipants - 1] = null;
                    numberOfParticipants--;

                    if (removedParticipant instanceof Student) {
                        this.numberOfStudents--;
                    }
                    if (removedParticipant instanceof Partner) {
                        this.numberOfPartners--;
                    }
                    if (removedParticipant instanceof Facilitator) {
                        this.numberOfFacilitators--;
                    }

                    return removedParticipant;
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Searches the array for a certain participant by their email and returns
     * the participant if found.
     *
     * @param email the email of the participant to search for
     * @return the participant if found, or null otherwise
     */
    @Override
    public Participant getParticipant(String email) {
        try {
            for (int i = 0; i < numberOfParticipants; i++) {
                if (participants[i].getEmail().equals(email)) {
                    return participants[i];
                }
            }

            throw new IllegalArgumentException("Participant doesn't exist");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Getter for the project tags
     *
     * @return the tags associated with the project
     */
    @Override
    public String[] getTags() {
        return this.tags;
    }

    /**
     * Checks whether or not the project is associated with a certain tag
     *
     * @param string the tag to be searched for
     * @return true if it has that tag, false if otherwise
     */
    @Override
    public boolean hasTag(String string) {
        for (String tag : this.tags) {
            if (tag != null) {
                if (tag.equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * adds a task to the array, granted it is not existant in the project, and
     * the number of tasks in the array has yet to reach its limit
     *
     * @param task task to be added to the task array
     * @throws IllegalNumberOfTasks if the maximum number of tasks has been
     *                              reached
     * @throws TaskAlreadyInProject if the task is already in the project
     */
    @Override
    public void addTask(Task task) throws IllegalNumberOfTasks, TaskAlreadyInProject {

        if (this.numberOfTasks >= this.maximum_tasks) {
            throw new IllegalNumberOfTasks("Maximum number of tasks reached.");
        }
        for (int i = 0; i < this.numberOfTasks; i++) {
            if (tasks[i] != null) {
                if (tasks[i].getTitle().equals(task.getTitle())) {
                    throw new TaskAlreadyInProject("Task already in project.");
                }
            }
        }

        this.tasks[this.numberOfTasks] = task;
        this.numberOfTasks++;

    }

    /**
     * removes a task from the array, granted it is exists in the project.
     *
     * @param task task to be added to the task array
     * @throws IllegalArgumentException the task doesn't exist.
     */
    public void removeTask(Task task) throws IllegalArgumentException {
        int a = 0;
        int lenght = tasks.length;
        for (int i = 0; i < tasks.length; i++) {

            if (tasks[i].equals(task)) {
                a = i;
            }
        }
        for (int j = a; j < tasks.length - 1; j++) {
            tasks[j] = tasks[j + 1];
        }
        tasks[lenght - 1] = null;
        this.numberOfTasks--;
    }

    /**
     * Returns a desired task
     *
     * @param title title of the task to be searched for
     * @return the task corresponding to the provided name
     */
    @Override
    public Task getTask(String title) {
        try {
            if (this.tasks.length == 0) {
                throw new NoTasksException("No tasks were found.");
            }
            for (int i = 0; i < this.numberOfTasks; i++) {
                if (this.tasks[i].getTitle().equals(title)) {
                    return tasks[i];
                }
            }
        } catch (NoTasksException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    /**
     * getter for the array of tasks
     *
     * @return the tasks in the project
     */
    @Override
    public Task[] getTasks() {
        return this.tasks;
    }

    /**
     * Checks if the project is completed by seeing if every task has at least
     * one submission generates a NoTasksException if there are no tasks in the
     * array
     *
     * @return true if it's completed, false if otherwise
     */
    @Override
    public boolean isCompleted() {
        int counter = 0;
        try {
            if (this.tasks.length == 0) {
                throw new NoTasksException("No tasks were found.");
            }
            for (int i = 0; i < this.numberOfTasks; i++) {
                int number = tasks[i].getNumberOfSubmissions();
                if (number != 0) {
                    counter++;
                }
            }
        } catch (NoTasksException ex) {
            System.out.println(ex.getMessage());
        }
        return counter >= numberOfTasks;
    }

    /**
     * Generates a string which shows how many tasks are completed within a
     * project, criteria being whether or not they have at least one submission
     * attached
     *
     * @return a string containing a project progress report
     */
    public String projectProgress() {
        String s = "";
        int counter = 0;
        for (int i = 0; i < this.numberOfTasks; i++) {
            int number = tasks[i].getNumberOfSubmissions();
            if (number != 0) {
                counter++;
            }
        }
        s += "Project progress report:\n";
        s += "Tasks completed (with submissions): " + counter + "out of" + this.numberOfTasks;
        return s;
    }

    /**
     * Provides valuable information about a project in string format
     *
     * @return valuable information about a project in string format
     */
    @Override
    public String toString() {
        String s = "";
        s += "Project name: " + this.name + "\n";
        s += "Project description: " + this.name + "\n";
        s += "Current participants: " + this.numberOfParticipants + "\n";
        for (int i = 0; i < this.numberOfTasks; i++) {
            s += ((MyTask)this.tasks[i]).toString() + "\n";
            s += "----------------" + "\n";
        }
        s += this.projectProgress();

        return s;
    }

    /**
     * Displays a list of all the students associated with a project, containing
     * their name and email
     */
    public void associatedStudents() {
        System.out.println("List of Students:\n");
        for (int i = 0; i < this.numberOfParticipants; i++) {
            if (this.participants[i] instanceof Student) {
                System.out.println("Name: " + participants[i].getName());
                System.out.println("Email" + participants[i].getEmail());
                System.out.println("--------------");
            }
        }
    }

    /**
     * Displays a list of all the facilitators associated with a project,
     * containing their name and email
     */
    public void associatedFacilitators() {
        System.out.println("List of Facilitators:\n");
        for (int i = 0; i < this.numberOfParticipants; i++) {
            if (this.participants[i] instanceof Facilitator) {
                System.out.println("Name: " + participants[i].getName());
                System.out.println("Email: " + participants[i].getEmail());
                System.out.println("--------------");
            }
        }
    }

    /**
     * Displays a list of all the partners associated with a project, containing
     * their name and email
     */
    public void associatedPartners() {
        System.out.println("List of Partners:\n");
        for (int i = 0; i < this.numberOfParticipants; i++) {
            if (this.participants[i] instanceof Partner) {
                System.out.println("Name: " + participants[i].getName());
                System.out.println("Email: " + participants[i].getEmail());
                System.out.println("--------------");
            }
        }
    }

    /**
     * Displays the incompleted tasks
     */
    public void displayIncomplete() {
        for (int i = 0; i < this.numberOfTasks; i++) {
            MyTask task = (MyTask) this.tasks[i];
            if (!task.isCompleted()) {
                System.out.println(task.getTitle());
            }

        }
    }

    /**
     * Displays the completed tasks
     */
    public void displayComplete() {
        for (int i = 0; i < this.numberOfTasks; i++) {
            MyTask task = (MyTask) this.tasks[i];
            if (task.isCompleted()) {
                System.out.println(task.getTitle());
            }

        }
    }
}
