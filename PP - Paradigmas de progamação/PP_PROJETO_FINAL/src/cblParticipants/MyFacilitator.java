/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is an implementation of the Facilitator interface, as well as an extension of the
 * MyParticipant class, which is an implementation of the participant interface, as per specified in the resources
 * provided.
 */

package cblParticipants;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Facilitator;
import ma02_resources.participants.Instituition;


public class MyFacilitator extends MyParticipant implements Facilitator {

    private String areaOfExpertise;

    /**
     * Constructor for a facilitator type object
     *
     * @param name            facilitator's name
     * @param email           facilitator's email
     * @param inst            facilitator's institution
     * @param cntct           facilitator's contact
     * @param areaOfExpertise facilitator's area of expertise
     */
    public MyFacilitator(String name, String email, Instituition inst, Contact cntct, String areaOfExpertise) {
        super(name, email, cntct, inst);
        this.areaOfExpertise = areaOfExpertise;
    }

    /**
     * Getter for the facilitator's area of expertise
     *
     * @return the facilitator's area of expertise
     */
    @Override
    public String getAreaOfExpertise() {
        return this.areaOfExpertise;
    }

    /**
     * Setter for the facilitator's area of expertise
     *
     * @param aOE the desired area of expertise
     */
    @Override
    public void setAreaOfExpertise(String aOE) {
        this.areaOfExpertise = aOE;
    }

}
