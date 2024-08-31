/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the participant management interaction options
 */

package userInteraction.menus;

import cblElements.MyEditionManagement;
import cblElements.MyProject;
import cblElements.exceptions.IllegalProjectException;
import ma02_resources.participants.Participant;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.exceptions.IllegalNumberOfParticipantType;
import ma02_resources.project.exceptions.ParticipantAlreadyInProject;
import userInteraction.UserInputHandler;


public class MenuParticipantManagement implements MenuDisplay {

    /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handleParticipantManagementMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {

        try {
            MenuParticipantManagement menuParticipantManagement = new MenuParticipantManagement();
            boolean isEditionManagementMenuRunning = true;

            String projectName;
            Project tempProject;
            Participant participant;
            Edition edition = editionsManager.getActiveEdition();

            while (isEditionManagementMenuRunning) {
                menuManager.displayMenu(menuParticipantManagement);

                int option = inputHandler.getIntStatic();

                switch (option) {
                    case 1:
                        projectName = inputHandler.searchProject();
                        tempProject = edition.getProject(projectName);
                        if(tempProject == null){
                            throw new IllegalProjectException("Project doesnt exist.");
                        }
                        participant = inputHandler.createParticipant();

                        try {
                            tempProject.addParticipant(participant);
                        } catch (IllegalNumberOfParticipantType e) {
                            System.out.println(e.getMessage());
                        } catch (ParticipantAlreadyInProject e) {
                            System.out.println(e.getMessage());
                        }
                        return;

                    case 2:
                        String name = inputHandler.searchParticipant();
                        projectName = inputHandler.searchProject();
                        tempProject = edition.getProject(projectName);
                        tempProject.removeParticipant(name);
                        System.out.println("Removed successfuly.");
                        return;

                    case 3:
                        projectName = inputHandler.searchProject();
                        tempProject = edition.getProject(projectName);
                        for (int i = 0; i < tempProject.getNumberOfParticipants(); i++) {
                            MyProject project = (MyProject) tempProject;
                            System.out.println(project.getParticipants()[i].getName());
                        }
                        System.out.println("-----------------");
                        return;

                    case 4:
                        isEditionManagementMenuRunning = false;
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
            catch ( IllegalProjectException ex){
                System.out.println(ex.getMessage());
                return;
            }
        }

    /**
     * this override of the display method is used to display the Participant menu
     */
    @Override
    public void display() {
        System.out.println("Edit Participants...");
        System.out.println("Option 1 - Register Participant");
        System.out.println("Option 2 - Remove Participant");
        System.out.println("Option 3 - List Participants");
        System.out.println("Option 4 - Return");
    }
}
