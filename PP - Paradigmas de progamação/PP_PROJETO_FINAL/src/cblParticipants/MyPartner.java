/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Partner interface, as well as an extension
 * of the MyParticipant class, as per specified in the provided
 * resources. It implements all the methods described in the interface necessary in order
 * to handle a Partner type object.
 */
package cblParticipants;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.Partner;

/**
 * @author juno
 */
public class MyPartner extends MyParticipant implements Partner {

    private final String vat;
    private final String website;

    /**
     * @param name    partner's name
     * @param email   partner's email
     * @param inst    partner's institution
     * @param cntct   partner's contact
     * @param vat     partner's VAT number
     * @param website partner's website
     */
    public MyPartner(String name, String email, Instituition inst, Contact cntct,
                     String vat, String website) {
        super(name, email, cntct, inst);
        this.vat = vat;
        this.website = website;
    }

    /**
     * Getter for the partner's VAT number
     *
     * @return the partner's VAT number
     */
    @Override
    public String getVat() {
        return this.vat;
    }

    /**
     * Getter for the partner's website
     *
     * @return the partner's website
     */
    @Override
    public String getWebsite() {
        return this.website;
    }

}
