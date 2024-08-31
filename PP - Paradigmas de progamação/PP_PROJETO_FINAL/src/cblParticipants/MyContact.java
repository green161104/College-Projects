/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is the implementation of the contact interface, as per provided in the resources.
 * It implements all of its methods in order to handle a Contact object.
 */
package cblParticipants;

import ma02_resources.participants.Contact;

public class MyContact implements Contact {

    private final String street;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String country;
    private final String phone;

    /**
     * Constructor for a contact type object
     *
     * @param street  street to be associated with the contact
     * @param city    city to be associated with the contact
     * @param state   state to be associated witht the contact
     * @param zipCode zip code to be associated with the contact
     * @param country country to be associated with the contact
     * @param phone   phone number to be associated with the contact
     */
    public MyContact(String street, String city, String state, String zipCode,
                     String country, String phone) {

        this.city = city;
        this.country = country;
        this.phone = phone;
        this.state = state;
        this.street = street;
        this.zipCode = zipCode;

    }

    /**
     * Getter for the street associated with the contact
     *
     * @return the street associated with the contact
     */
    @Override
    public String getStreet() {
        return this.street;
    }

    /**
     * Getter for the city associated with the contact
     *
     * @return the city associated with the contact
     */
    @Override
    public String getCity() {
        return this.city;
    }

    /**
     * Getter for the state associated with the contact
     *
     * @return the state associated with the contact
     */
    @Override
    public String getState() {
        return this.state;
    }

    /**
     * Getter for the zip code associated with the contact
     *
     * @return the zip code associated with the contact
     */
    @Override
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * Getter for the country associated with the contact
     *
     * @return the country associated with the contact
     */
    @Override
    public String getCountry() {
        return this.country;
    }

    /**
     * Getter for the phone number associated with the contact
     *
     * @return the phone number associated with the contact
     */
    @Override
    public String getPhone() {
        return this.phone;
    }

}
