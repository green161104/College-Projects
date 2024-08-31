/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This interface defines the contract for managing an array of CBL editions, and obtain information
 * stored in it.
 **/
package interfaces;

import ma02_resources.project.Edition;
import ma02_resources.project.Project;


public interface EditionsManagement {

    public int getNumberOfEditions();

    public Edition[] getEditions();

    public int getEdition(String edition);

    public void addEdition(Edition edition);

    public void removeEdition(String edition);

    public Edition getActiveEdition();

    public void setActiveEdition(String edition);

    public Project[] getIncompleteProjectsFromActive();

    public Project[] getIncompleteProjectsFromEdition(Edition edition);

    public Edition[] getIncompleteEditions();

}
