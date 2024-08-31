/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class pertains to the management of an array of CBL editions,it's the
 * implementation of the EditionsManagement interface.
 * As such, it implements all the methods described in that interface necessary for the management
 * of editions.
 */
package cblElements;

import cblElements.exceptions.*;
import interfaces.EditionsManagement;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Status;

public class MyEditionManagement implements EditionsManagement {

    private final int MAX_EDITIONS = 50;
    private int numberOfEditions;
    private Edition activeEdition;
    private Edition[] editions;

    /**
     * Constructor for the class, initializes the array
     */
    public MyEditionManagement() {
        this.editions = new Edition[MAX_EDITIONS];
    }

    /**
     * Getter for the number of editions
     *
     * @return the number of editions
     */
    @Override
    public int getNumberOfEditions() {
        return this.numberOfEditions;
    }

    /**
     * Getter for the array of editions
     *
     * @return the array of editions
     */
    @Override
    public Edition[] getEditions() {
        return this.editions;
    }

    /**
     * Method used to find a specific edition
     *
     * @param edition the edition to be searched
     * @return the index of the edition
     */
    @Override
    public int getEdition(String edition) {
        for (int i = 0; i < this.numberOfEditions; i++) {
            if (this.editions[i].getName().equals(edition)) {
                return i;
            }
        }
        return -1;
    }

    @Override

    /**
     Adds an edition to the array, granted the array isn't full, and the edition doesn't already exist
     */
    public void addEdition(Edition edition) {
        try {
            if(edition == null){
                throw new NullPointerException("Edition invalid.");
            }
            if (this.getEdition(edition.getName()) == -1) {
                if (this.numberOfEditions < this.MAX_EDITIONS) {
                    this.editions[this.numberOfEditions] = edition;
                    this.numberOfEditions++;
                    System.out.println("Edition added successfully");
                } else {
                    throw new EditionsFullException("Editions are full. Please delete an older one to add.");
                }
            } else {
                throw new IllegalEditionException("Edition already exists.");
            }
        } catch (EditionsFullException | IllegalEditionException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Revmoves an edition from the array, after looking for it with the getEdition() method
     *
     * @param edition edition to be removed
     */
    @Override
    public void removeEdition(String edition) {
        int editionToRemove;
        editionToRemove = this.getEdition(edition);
        try {
            if (editionToRemove != -1) {
                for (int i = editionToRemove; i < this.numberOfEditions - 1; i++) {
                    this.editions[i] = this.editions[i + 1];

                }
                this.editions[numberOfEditions] = null;
                this.numberOfEditions--;
                System.out.println("Edition successfully removed");
            } else {
                throw new IllegalEditionException("Edition doesn't exist.");
            }
        } catch (IllegalEditionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Getter for the active edition
     *
     * @return the currently active edition
     */
    @Override
    public Edition getActiveEdition() {
        return this.activeEdition;
    }

    /**
     * Sets the active edition, making sure no other editions are active and the edition exists
     *
     * @param edition edition to be set as active
     */
    @Override
    public void setActiveEdition(String edition) {
        int editionToAlter = this.getEdition(edition);
        try {
            if (this.numberOfEditions == 0) {
                throw new NoEditionsException("No editions found.");
            }

            if (editionToAlter == -1) {
                throw new EditionNotFoundException("Edition not found.");
            }

            for (int i = 0; i < this.numberOfEditions; i++) {
                if (this.editions[i].getStatus() == Status.ACTIVE) {
                    throw new EditionAlreadyActiveException("There's already an active edition. "
                            + "Please make sure no editions are active before trying to activate one.");
                }
            }

            for (int i = 0; i < this.numberOfEditions; i++) {
                if (this.editions[i].equals(this.editions[editionToAlter])) {
                    this.editions[i].setStatus(Status.ACTIVE);
                    this.activeEdition = this.editions[i];
                    System.out.println("Edition successfully set as active.");
                    return;  // Exit the loop after setting the active edition
                }
            }

            // If no matching edition is found
            throw new EditionNotFoundException("Edition not found.");
        } catch (NoEditionsException | EditionAlreadyActiveException | EditionNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }




    /**
     * Searches the projects of the active edition, and returns an array
     * containing those that aren't finished The criteria for whether a project
     * is finished or not, is whether there's at least one submission per task
     *
     * @return an array of all the unfinished projects within the active edition
     */
    @Override
    public Project[] getIncompleteProjectsFromActive() {
        Project[] unfinished = new Project[numberOfEditions * 20];
        int unfinishedIndex = 0;
        try {
            if (this.editions.length == 0) {
                throw new NoEditionsException("No editions found");
            }
            for (int i = 0; i < numberOfEditions; i++) {
                if (editions[i].getStatus().equals(Status.ACTIVE)) {
                    for (int j = 0; j < editions[i].getNumberOfProjects(); j++) {
                        if (editions[i].getNumberOfProjects() == 0) {
                            throw new NoProjectsException("No projects found");
                        }
                        boolean projectUnfinished = editions[i].getProjects()[j].isCompleted();

                        if (projectUnfinished) {
                            unfinished[unfinishedIndex] = editions[i].getProjects()[j];
                            unfinishedIndex++;
                        }
                    }
                }
            }

        } catch (NoEditionsException | NoProjectsException ex) {
            System.out.println(ex.getMessage());
        }
        Project[] trimmedArray = new Project[unfinishedIndex];
        System.arraycopy(unfinished, 0, trimmedArray, 0, unfinishedIndex);
        return trimmedArray;
    }

    /**
     * Searches the projects within an edition and returns an array containing
     * those that are finished. The criteria for whether a project is finished
     * or not, is whether there's at least one submission per task
     *
     * @param edition edition to be searched for unfinished projects.
     * @return an array of unfinished projects
     */
    @Override
    public Project[] getIncompleteProjectsFromEdition(Edition edition) {

        Project[] unfinished = new Project[edition.getNumberOfProjects()];
        int unfinishedIndex = 0;
        try {
            if (this.editions.length == 0) {
                throw new NoEditionsException("No editions found");
            }
            if (edition.getNumberOfProjects() == 0) {
                throw new NoProjectsException("No projects found");
            }
            for (int j = 0; j < edition.getNumberOfProjects(); j++) {

                boolean projectUnfinished = edition.getProjects()[j].isCompleted();

                if (projectUnfinished) {
                    unfinished[unfinishedIndex] = edition.getProjects()[j];
                    unfinishedIndex++;
                }
            }

        } catch (NoEditionsException | NoProjectsException ex) {
            System.out.println(ex.getMessage());
        }

        Project[] trimmedArray = new Project[unfinishedIndex];
        System.arraycopy(unfinished, 0, trimmedArray, 0, unfinishedIndex);
        return trimmedArray;
    }

    /**
     * Iterates through the array of editions, calling the
     * getIncompleteProjectsFromEdition for each one, and adding every edition
     * with unfinished projects into an array
     *
     * @return an array containing every edition with unfinished projects
     */
    @Override
    public Edition[] getIncompleteEditions() {
        Edition[] unfinished = new Edition[this.MAX_EDITIONS];
        int unfinishedIndex = 0;
        try {
            if (this.editions.length == 0) {
                throw new NoEditionsException("No editions found");
            }
            for (int i = 0; i < this.numberOfEditions; i++) {
                Project[] uncompleteProjects = getIncompleteProjectsFromEdition(editions[i]);
                if (uncompleteProjects.length > 0) {
                    unfinished[unfinishedIndex] = editions[i];
                    unfinishedIndex++;
                }
            }

        } catch (NoEditionsException e) {
            System.out.println(e.getMessage());
        }
        Edition[] trimmedArray = new Edition[unfinishedIndex];
        System.arraycopy(unfinished, 0, trimmedArray, 0, unfinishedIndex);
        return trimmedArray;
    }

}
