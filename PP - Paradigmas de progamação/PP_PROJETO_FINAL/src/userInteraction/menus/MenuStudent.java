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

import cblElements.CBLEdition;
import cblElements.MyEditionManagement;
import cblElements.exceptions.IllegalSubmissionException;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Submission;
import ma02_resources.project.Task;
import userInteraction.UserInputHandler;

public class MenuStudent implements MenuDisplay {
   /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handleStudentMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {
        try {
            MenuStudent menuStudent = new MenuStudent();
            boolean isStudentMenuRunning = true;

            Edition edition = editionsManager.getActiveEdition();
            Project[] tempProjects = edition.getProjects();
            CBLEdition tempEdition = (CBLEdition) edition;

            while (isStudentMenuRunning) {
                menuManager.displayMenu(menuStudent);

                int option = inputHandler.getIntStatic();

                switch (option) {
                    case 1:
                        System.out.println(tempEdition.projectsSummary() + "\n");
                        return;
                    case 2:
                        try {
                            String projectName = inputHandler.searchProject();
                            Project project = edition.getProject(projectName);
                            if (project != null) {
                                String taskName = inputHandler.searchTask();
                                Task task = project.getTask(taskName);
                                if (task != null) {
                                    try {
                                        Submission submission = inputHandler.createSubmission(project);
                                        task.addSubmission(submission);
                                        System.out.println("submission added");
                                    } catch (IllegalSubmissionException ex) {
                                        System.out.println("Unable to create submission: " + ex.getMessage());
                                    }
                                } else {
                                    System.out.println("Task not found. Please try again.");
                                }
                            } else {
                                System.out.println("Project not found. Please try again.");
                            }
                        } catch (Exception ex) {
                            System.out.println("An error occurred: " + ex.getMessage());
                        }
                        return;
                    case 3:
                        isStudentMenuRunning = false; //goes out of studentMenu
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
        System.out.println("welcome to the student menu");
        System.out.println("Please Pick your option");
        System.out.println("1 - Check Projects and Tasks");
        System.out.println("2 - Turn in Submissions");
        System.out.println("3 - Voltar");
    }
}
