/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Participant interface, as per specified in the provided
 * resources. It implements all the methods described in the interface necessary in order
 * to handle a Participant
 * type object.
 */
package cblParticipants;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Participant;


public class MyParticipant implements Participant {

    private final String name;
    private final String email;
    private Contact contact;
    private Instituition institution;

    /**
     * Constructor for the MyParticipant class
     *
     * @param name        participant's name
     * @param email       participant's email
     * @param contact     participant's contact
     * @param institution participant's institution
     */
    public MyParticipant(String name, String email, Contact contact, Instituition institution) {
        this.name = name;
        this.contact = contact;
        this.email = email;
        this.institution = institution;
    }

    /**
     * Getter for the participant's name
     *
     * @return the participant's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the participant's email
     *
     * @return the participant's email
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter for the participant's contact
     *
     * @return the participant's contact
     */
    @Override
    public Contact getContact() {
        return this.contact;
    }

    /**
     * Setter for the participant's contact
     *
     * @param cntct desired contact
     */
    @Override
    public void setContact(Contact cntct) {
        this.contact = cntct;
    }

    /**
     * Getter for the institution
     *
     * @return the participant's institution
     */
    @Override
    public Instituition getInstituition() {
        return this.institution;
    }

    /**
     * Setter for the participant's institution
     *
     * @param instn desired institution
     */
    @Override
    public void setInstituition(Instituition instn) {
        this.institution = instn;
    }

}
