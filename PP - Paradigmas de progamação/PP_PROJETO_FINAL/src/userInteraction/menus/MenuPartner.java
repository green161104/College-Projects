/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the partner interaction options, and lead further into a submenu.
 */

package userInteraction.menus;

import cblElements.CBLEdition;
import cblElements.MyEditionManagement;
import cblElements.MyProject;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import userInteraction.UserInputHandler;

public class MenuPartner implements MenuDisplay {
    
    /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handlePartnerMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {
        try {

            MenuPartner menuPartner = new MenuPartner();
            boolean isPartnerMenuRunning = true;

            Edition edition = editionsManager.getActiveEdition();
            Project[] tempProjects = edition.getProjects();
            CBLEdition tempEdition = (CBLEdition) edition;

            while (isPartnerMenuRunning) {
                menuManager.displayMenu(menuPartner);

                int option = inputHandler.getIntStatic();

                switch (option) {
                    case 1:
                        System.out.println(((CBLEdition) edition).editionProgress());
                        return;
                    case 2:
                        for (int i = 0; i < tempEdition.getNumberOfProjects(); i++) {
                            MyProject tempProject = (MyProject) tempProjects[i];
                            System.out.println(tempProject.getName());
                            tempProject.associatedPartners();
                            System.out.println("------------");
                        }
                        return;
                    case 3:
                        isPartnerMenuRunning = false; //goes out of menuAdmin
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }

        } catch (NullPointerException ex) {
            System.out.println("Elements not properly initialized.");
            return;
        }
    }

    /**
     * this override of the display method is used to display the Participant menu
     */
    @Override
    public void display() {
        System.out.println("welcome to the partner menu");
        System.out.println("Please Pick your option");
        System.out.println("1 - Edition Information ");
        System.out.println("2 - Other Partners");
        System.out.println("3 - Return");
    }
}
