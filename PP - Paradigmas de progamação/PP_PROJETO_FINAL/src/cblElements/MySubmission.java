/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is an implementation of the Submission interface, as per specified in the resources
 * provided to us. It implements all of the necessary methods to manage a submission object as well
 * as all the information it contains.
 **/
package cblElements;

import ma02_resources.participants.Student;
import ma02_resources.project.Submission;

import java.time.LocalDateTime;


public class MySubmission implements ma02_resources.project.Submission {

    private final LocalDateTime date;
    private final Student student;
    private final String text;

    /**
     * Constructor for the MySubmission class
     *
     * @param date    date for the submission
     * @param student student which submitted
     * @param text    body of the submission
     */
    public MySubmission(LocalDateTime date, String text, Student student) {
        this.date = date;
        this.text = text;
        this.student = student;
    }


    /**
     * Getter for the date of submission
     *
     * @return this submission's date
     */
    @Override
    public LocalDateTime getDate() {
        return this.date;
    }

    /**
     * Getter for the submitting student
     *
     * @return the submitting student
     */
    @Override
    public Student getStudent() {
        return this.student;
    }

    /**
     * Getter for the text body of the submission
     *
     * @return the text of the submission
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * Compares two submissions based on the date
     *
     * @param sbmsn submission to compare this one to
     * @return -1 if this one is prior to the one being compared, 1 if after, and 0 if they're submitted at the same time
     */
    @Override
    public int compareTo(Submission sbmsn) {
        if (this.date.isBefore(sbmsn.getDate())) {
            return -1;
        }
        if (this.date.isAfter(sbmsn.getDate())) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * implementation of the toString method for the MySubmission class
     *
     * @return a text representation of a MySubmission object
     */
    @Override
    public String toString() {
        String s = "";
        s += "Body of submission: " + this.text;
        s += "Submitted by: " + this.student.getName();

        return s;
    }

}
