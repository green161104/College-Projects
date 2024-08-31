/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the administrator interaction options, and lead further into a submenu.
 */

package userInteraction.menus;

import cblElements.MyEditionManagement;
import userInteraction.UserInputHandler;


public class MenuAdmin implements MenuDisplay {
    
  /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handleAdminMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {

        MenuAdmin menuAdmin = new MenuAdmin();
        boolean isAdminMenuRunning = true;

        while (isAdminMenuRunning) {

            menuManager.displayMenu(menuAdmin);

            int option = inputHandler.getIntStatic();

            switch (option) {
                case 1:
                    MenuEditionManagement.handeEditionManagementMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 2:
                    MenuParticipantManagement.handleParticipantManagementMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 3:
                    MenuProjectManagement.handleProjectManagentMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 4:
                    MenuTaskManagement.handleTaskManagementMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 5:
                    isAdminMenuRunning = false; //goes out of menuAdmin
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");

            }
        }

    }

    /**
     * this override of the display method is used to display the administrator menu
     */
    @Override
    public void display() {
        System.out.println("Welcome to the Admin menu");
        System.out.println("Please pick your option:");
        System.out.println("1 - Manage Editions ");
        System.out.println("2 - Manage Participants");
        System.out.println("3 - Manage Projects");
        System.out.println("4 - Manage Tasks");
        System.out.println("5 - Return");

    }
}
