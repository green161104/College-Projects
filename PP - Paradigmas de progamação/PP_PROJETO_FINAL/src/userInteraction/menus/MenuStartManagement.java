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

public class MenuStartManagement implements MenuDisplay {
    /**
     * @param menuManager     Necessary to handle all the menu operations
     *
     */
    public static void handleStartMenu(MenuManager menuManager) {
        UserInputHandler inputHandler = new UserInputHandler();
        MenuStartManagement menuStart = new MenuStartManagement();
        MyEditionManagement editionsManager = new MyEditionManagement();
        boolean isRunning = true;

        while (isRunning) {

            menuManager.displayMenu(menuStart);

            int option = inputHandler.getIntStatic();

            switch (option) {
                case 1:
                    MenuAdmin.handleAdminMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 2:
                    MenuStudent.handleStudentMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 3:
                    MenuFacilitator.handeFacilitatorMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 4:
                    MenuPartner.handlePartnerMenu(menuManager, editionsManager, inputHandler);
                    break;
                case 5:
                    isRunning = false;
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

    }

    /**
     * this override of the display method is used to display the Participant menu
     */
    @Override
    public void display() {
        System.out.println("Bien venido al BCL JJ (Juno & Joao)");
        System.out.println("Please Pick your option");
        System.out.println("1 - Admin menu;");
        System.out.println("2 - Student menu;");
        System.out.println("3 - Facilitator menu;");
        System.out.println("4 - Partner menu;");
        System.out.println("5 - Quit;");
    }
}
