/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the Instituition interface, as per specified in the provided
 * resources. It implements all the methods described in the interface necessary in order
 * to handle an Instituition type object.
 */
package cblParticipants;

import ma02_resources.participants.Contact;
import ma02_resources.participants.Instituition;
import ma02_resources.participants.InstituitionType;

public class MyInstitution implements Instituition {

    private final String name;
    private final String email;
    private InstituitionType type;
    private Contact contact;
    private String website;
    private String description;

    /**
     * @param name        the institution's name
     * @param email       the institution's email
     * @param type        the institution's type
     * @param contact     the institution's contact
     * @param website     the institution's website
     * @param description the institution's description
     */
    public MyInstitution(String name, String email, InstituitionType type, Contact contact,
                         String website, String description) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.type = type;
        this.website = website;
        this.description = description;
    }

    /**
     * Getter for the institution's name
     *
     * @return the institution's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the institution's email
     *
     * @return the institution's email
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Getter for the institution's type
     *
     * @return the institution's type
     */
    @Override
    public InstituitionType getType() {
        return this.type;
    }

    /**
     * Setter for the institution type
     *
     * @param it desired institution type
     */
    @Override
    public void setType(InstituitionType it) {
        this.type = it;
    }

    /**
     * Getter for the institution's contact
     *
     * @return the institution's contact
     */
    @Override
    public Contact getContact() {
        return this.contact;
    }

    /**
     * Setter for the institutional contact
     *
     * @param cntct desired institutional contact
     */
    @Override
    public void setContact(Contact cntct) {
        this.contact = cntct;
    }

    /**
     * Getter for the institution's website
     *
     * @return the institution's website
     */
    @Override
    public String getWebsite() {
        return this.website;
    }

    /**
     * Setter for the institutional website
     *
     * @param website desired institutional website
     */
    @Override
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Getter for the institution's description
     *
     * @return the institution's description
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for the institution's description
     *
     * @param description desired institution's description
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

}
