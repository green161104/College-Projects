/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Task interface, as per specified in the resources provided.
 * It implements all the methods necessary to handle a task object as well as he subsequent array of submissions
 */
package cblElements;

import cblElements.exceptions.IllegalSubmissionException;
import ma02_resources.project.Submission;
import ma02_resources.project.Task;

import java.time.LocalDate;

public class MyTask implements ma02_resources.project.Task {

    private final int EXPAND_FACTOR = 2;
    private final int MAX_SUBMISSIONS = 10;
    private final String title;
    private final String description;
    private final LocalDate taskStart;
    private int duration;
    private Submission[] submissions;
    private int numberOfSubmissions;
    private LocalDate end;

    /**
     * @param title       title for the task
     * @param description description for the task
     * @param duration    duration of the task, in a number of days
     * @param taskStart   starting date of the task
     */
    public MyTask(String title, String description, int duration, LocalDate taskStart) {

        this.title = title;
        this.description = description;
        this.duration = duration;
        this.end = taskStart.plusDays(duration);
        this.taskStart = taskStart;
        this.submissions = new Submission[MAX_SUBMISSIONS];

    }

    /**
     * getter for the starting date of the task
     *
     * @return the starting date of the task
     */
    @Override
    public LocalDate getStart() {
        return this.taskStart;
    }

    /**
     * Getter for the end date of the task
     *
     * @return the end date for the task
     */
    @Override
    public LocalDate getEnd() {
        return this.end;
    }

    /**
     * Getter for the duration of the task
     *
     * @return the duration of the task in days
     */
    @Override
    public int getDuration() {
        return this.duration;
    }

    /**
     * getter for the title of the task
     *
     * @return the task's title
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * getter for the description of the task
     *
     * @return the task's description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Getter for the array of submissions
     *
     * @return the array of submissions for this task
     */
    @Override
    public Submission[] getSubmissions() {
        return this.submissions;
    }

    /**
     * getter for the number of submissions
     *
     * @return the number of submissions for this task
     */
    @Override
    public int getNumberOfSubmissions() {
        return this.numberOfSubmissions;
    }

    /**
     * adds a submission to the array, granted it is valid. if the submission array is full,
     * it gets expanded by a factor of 2, by calling the expandSubmissionsArray method
     * present in this class
     *
     * @param sbmsn submission to be added
     */
    @Override
    public void addSubmission(Submission sbmsn) {
        try {
            if (this.numberOfSubmissions >= this.MAX_SUBMISSIONS) {
                expandSubmissionsArray();
            }

            if (sbmsn == null) {
                throw new IllegalSubmissionException("Submission not valid.");
            }

            this.submissions[this.numberOfSubmissions] = sbmsn;
            this.numberOfSubmissions++;

        } catch (IllegalSubmissionException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Expands the array of submissions by a factor of 2, which is declared as a constant in the class.
     * Works by creating a temporary array which then gets assigned to the submissions array, retaining all
     * previous information it held pre-expansion.
     */
    private void expandSubmissionsArray() {
        int newCapacity = this.submissions.length * EXPAND_FACTOR;
        Submission[] expandedArray = new Submission[newCapacity];
        System.arraycopy(this.submissions, 0, expandedArray, 0, this.numberOfSubmissions);
        this.submissions = expandedArray;
        System.out.println("Submissions array expanded to size: " + newCapacity);
    }

    /**
     * Extends the deadline for submissions by a certain number of days. Throws an IllegalArgumentException
     * in case the number of days is negative
     *
     * @param i number of days to add
     */
    @Override
    public void extendDeadline(int i) {
        try {
            if (i < 0) {
                throw new IllegalArgumentException("Invalid days.");
            }
            duration = duration + i;
            this.end = end.plusDays(i);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Compares two tasks using their starting dates as criteria
     *
     * @param task the task to be compared to the current instance
     * @return 1 if this task starts after, -1 if it starts before, 0 otherwise
     */
    @Override
    public int compareTo(Task task) {
        if (!this.taskStart.isAfter(task.getStart())) {
            return 1;
        }
        if (!this.taskStart.isBefore(task.getStart())) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * If the task has at least one submission attached, it is counted as completed
     *
     * @return true if it is completed, false otherwise
     */
    public boolean isCompleted() {
        return this.numberOfSubmissions != 0;
    }

    @Override
    public String toString() {
        String s = "";
        s += "Task title: " + this.title + "\n";
        s += "Description: " + this.description + "\n";
        s += "Starting date: " + this.taskStart.toString() + "\n";
        s += "Deadline: " + this.end.toString() + "\n";
        return s;
    }
}
