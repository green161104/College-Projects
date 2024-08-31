/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the project management interaction options
 **/


package userInteraction.menus;

import cblElements.CBLEdition;
import cblElements.MyEditionManagement;
import ma02_resources.project.Edition;
import userInteraction.UserInputHandler;

public class MenuProjectManagement implements MenuDisplay {
    
 /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handleProjectManagentMenu(MenuManager menuManager, MyEditionManagement editionsManager,UserInputHandler inputHandler) {
        try {

            MenuProjectManagement menuProjectManagement = new MenuProjectManagement();
            boolean isProjectManagementMenuRunning = true;
            Edition edition = editionsManager.getActiveEdition();

            while (isProjectManagementMenuRunning) {
                menuManager.displayMenu(menuProjectManagement);

                int option = inputHandler.getIntStatic();

                switch (option) {
                    case 1:
                        ((CBLEdition) edition).createProject();
                        return;
                    case 2:
                        String name = inputHandler.searchProject();
                        ((CBLEdition) edition).removeProject(name);
                        return;

                    case 3:
                        System.out.println(((CBLEdition) edition).projectsSummary());
                        return;

                    case 4:
                        isProjectManagementMenuRunning = false;
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

    @Override
    public void display() {
        System.out.println("Edit projects...");
        System.out.println("PLEASE pick your option:");
        System.out.println("1 - Add Project ");
        System.out.println("2 - Remove Project");
        System.out.println("3 - List Projects");
        System.out.println("4 - return");
    }
}
