/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the edition management interaction options
 **/

package userInteraction.menus;

import cblElements.MyEditionManagement;
import ma02_resources.project.Edition;
import ma02_resources.project.Status;
import userInteraction.UserInputHandler;


public class MenuEditionManagement implements MenuDisplay {

 /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handeEditionManagementMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {

        MenuEditionManagement menuEditionManagement = new MenuEditionManagement();

        boolean isEditionManagementMenuRunning = true;

        while (isEditionManagementMenuRunning) {
            menuManager.displayMenu(menuEditionManagement);

            int option = inputHandler.getIntStatic();

            switch (option) {
                case 1:
                    Edition edition = inputHandler.createEdition();
                    editionsManager.addEdition(edition);
                    return;

                case 2:
                    String name = inputHandler.searchEdition();
                    editionsManager.removeEdition(name);

                    return;

                case 3:
                    String editionToSetActive = inputHandler.searchEdition();
                    editionsManager.setActiveEdition(editionToSetActive);

                    return;

                case 4:
                    String editionToAlter = inputHandler.searchEdition();
                    int tempEdition = editionsManager.getEdition(editionToAlter);
                    Status status = inputHandler.changeStatus();
                    editionsManager.getEditions()[tempEdition].setStatus(status);

                    System.out.println("Edition set as" + editionsManager.getEditions()[tempEdition].getStatus());

                case 5:
                    for (int i = 0; i < editionsManager.getNumberOfEditions(); i++) {
                        System.out.println(editionsManager.getEditions()[i].getName());
                        System.out.println("----");
                    }
                case 6:
                    isEditionManagementMenuRunning = false;
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

    }

    /**
     * this override of the display method is used to display the Edition Management menu
     */
    @Override
    public void display() {
        System.out.println("Edit Editions...");
        System.out.println("Option 1 - Create Edition");
        System.out.println("Option 2 - Remove Edtion");
        System.out.println("Option 3 - Alter Active Edition");
        System.out.println("Option 4 - Change edition status");
        System.out.println("Option 5 - List Editions");
        System.out.println("Option 4 - Return");
    }
}
