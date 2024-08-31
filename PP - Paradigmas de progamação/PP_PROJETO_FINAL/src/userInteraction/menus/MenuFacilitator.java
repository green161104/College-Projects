/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the facilitator interaction options
 */

package userInteraction.menus;

import cblElements.CBLEdition;
import cblElements.MyEditionManagement;
import cblElements.MyProject;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import userInteraction.UserInputHandler;

public class MenuFacilitator implements MenuDisplay {

     /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handeFacilitatorMenu(MenuManager menuManager, MyEditionManagement editionsManager,  UserInputHandler inputHandler) {
        try {

            MenuFacilitator menuFacilitator = new MenuFacilitator();
            Edition edition = editionsManager.getActiveEdition();
            Project[] tempProjects = edition.getProjects();

            boolean isFacilitatorMenuRunning = true;

            while (isFacilitatorMenuRunning) {
                menuManager.displayMenu(menuFacilitator);

                int option = inputHandler.getIntStatic();

                switch (option) {

                    case 1:
                        System.out.println(((CBLEdition) edition).projectsSummary() + "\n");
                        return;

                    case 2:
                        System.out.println((((CBLEdition) edition).editionProgress()));
                        return;

                    case 3:
                        Project[] tempIncomplete = editionsManager.getIncompleteProjectsFromActive();
                        for (int i = 0; i < tempIncomplete.length; i++) {
                            if (tempIncomplete[i] != null) {
                                ((MyProject) tempIncomplete[i]).toString();
                            }
                        }
                        return;

                    case 4:
                        String editionName = inputHandler.searchEdition();
                        int index = editionsManager.getEdition(editionName);
                        tempIncomplete = editionsManager.getIncompleteProjectsFromEdition(editionsManager.getEditions()[index]);
                        for (int i = 0; i < tempIncomplete.length; i++) {
                            if (tempIncomplete[i] != null) {
                                ((MyProject) tempIncomplete[i]).toString();
                            }
                        }
                        return;

                    case 5:
                        for (int i = 0; i < edition.getNumberOfProjects(); i++) {
                            MyProject tempProject = (MyProject) tempProjects[i];
                            System.out.println(tempProject.getName());
                            tempProject.displayComplete();
                            System.out.println("------------");
                        }
                        return;


                    case 6:
                        for (int i = 0; i < edition.getNumberOfProjects(); i++) {
                            MyProject tempProject = (MyProject) tempProjects[i];
                            System.out.println(tempProject.getName());
                            tempProject.displayIncomplete();
                            System.out.println("------------");
                        }
                        return;

                    case 7:
                        for (int i = 0; i < edition.getNumberOfProjects(); i++) {
                            MyProject tempProject = (MyProject) tempProjects[i];
                            System.out.println(tempProject.getName());
                            tempProject.associatedStudents();
                            System.out.println("------------");
                        }

                        return;

                    case 8:
                        String participant = inputHandler.searchParticipant();
                        Project[] projectsOf = edition.getProjectsOf(participant);
                        for (int i = 0; i < projectsOf.length; i++) {
                            if (projectsOf[i] != null) {
                                System.out.println(((MyProject) projectsOf[i]).toString());
                            }
                        }
                        return;

                    case 9:
                        for (int i = 0; i < edition.getNumberOfProjects(); i++) {
                            MyProject tempProject = (MyProject) tempProjects[i];
                            System.out.println(tempProject.getName());
                            tempProject.associatedFacilitators();
                            System.out.println("------------");
                        }
                        return;

                    case 0:
                        isFacilitatorMenuRunning = false; //goes out of menuAdmin
                        return;


                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }

        } catch (NullPointerException e) {
            System.out.println("Elements not properly initialized.");
            return;
        }
    }

    /**
     * this override of the display method is used to display the Facilitator menu
     */
    @Override
    public void display() {
        System.out.println("Welcome to the Facilitator Menu");
        System.out.println("Please Pick your option");
        System.out.println("1 - List Projects");
        System.out.println("2 - Edition Summary");
        System.out.println("3 - List incomplete projects in active edition");
        System.out.println("4 - List incomplete projects in an edition");
        System.out.println("5 - List Completed Tasks from all projects ");
        System.out.println("6 - List Incomplete Tasks from all projects");
        System.out.println("7 - List Students");
        System.out.println("8 - List Partners");
        System.out.println("9 - List Facilitators");
        System.out.println("0 - Return");
    }
}
