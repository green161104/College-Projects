/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Student interface, as well as an extension
 * of the MyPartner class, as per specified in the provided
 * resources. It implements all the methods described in the interface necessary in order
 * to handle a Student type object.
 */
package cblParticipants;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Student;

/**
 * @author juno
 */
public class MyStudent extends MyParticipant implements Student {

    private final int studentNumber;

    /**
     * Constructor for a MyStudent type object
     *
     * @param name student's name
     * @param email student's email
     * @param inst student's institution
     * @param cntct student's contact
     * @param number student's number
     */
    public MyStudent(String name, String email, Instituition inst, Contact cntct, int number) {
        super(name, email, cntct, inst);
        this.studentNumber = number;
    }

    /**
     * getter for student number
     * @return student's number
     */
    @Override
    public int getNumber() {
        return this.studentNumber;
    }

}
