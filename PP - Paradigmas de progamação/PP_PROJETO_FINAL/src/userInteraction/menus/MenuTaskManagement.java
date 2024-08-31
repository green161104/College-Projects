/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class is destined to display the menu with the task management interaction options
 **/

package userInteraction.menus;

import cblElements.MyEditionManagement;
import cblElements.MyProject;
import cblElements.exceptions.IllegalProjectException;
import ma02_resources.project.Edition;
import ma02_resources.project.Project;
import ma02_resources.project.Task;
import ma02_resources.project.exceptions.IllegalNumberOfTasks;
import ma02_resources.project.exceptions.TaskAlreadyInProject;
import userInteraction.UserInputHandler;

public class MenuTaskManagement implements MenuDisplay {
     /**
   * 
   * @param menuManager necessary for menu operations
   * @param editionsManager necessary for management of editions array
   * @param inputHandler necessary for input handling
   */
    public static void handleTaskManagementMenu(MenuManager menuManager, MyEditionManagement editionsManager, UserInputHandler inputHandler) {
        try {
            MenuTaskManagement menuTaskManagement = new MenuTaskManagement();
            boolean isTaskManagementMenuRunning = true;
            String projectName;
            Project projectTemp;

            Edition edition = editionsManager.getActiveEdition();

            while (isTaskManagementMenuRunning) {
                menuManager.displayMenu(menuTaskManagement);

                int option = inputHandler.getIntStatic();

                switch (option) {
                    case 1:
                        boolean a = true;
                        while (a) {
                            projectName = inputHandler.searchProject();
                            projectTemp = edition.getProject(projectName);
                            if (projectTemp == null) {
                                throw new IllegalProjectException("Project doesnt exist.");
                            }
                            Task task1 = inputHandler.createTask();

                            try {
                                projectTemp.addTask(task1);
                            } catch (IllegalNumberOfTasks e) {
                                throw new RuntimeException(e);
                            } catch (TaskAlreadyInProject e) {
                                throw new RuntimeException(e);
                            }
                            System.out.println("continue?\n 1 - yes \n 2 - no");
                            int z = inputHandler.getIntStatic();
                            if (z == 1) {
                            }
                            if (z == 2) {
                                a = false;
                            }
                            if (z != 1 && z != 2) {
                                System.out.println("Invalid option. Please try again.");
                            }

                        }
                        return;

                    case 2:
                        boolean b = true;
                        while (b) {
                            projectName = inputHandler.searchProject();
                            projectTemp = edition.getProject(projectName);
                            String name = inputHandler.searchTask();
                            Task taskToRemove = projectTemp.getTask(name);
                            ((MyProject) projectTemp).removeTask(taskToRemove);
                            System.out.println("continue?\n 1 - yes \n 2 - no");

                            int z = inputHandler.getIntStatic();
                            if (z == 1) {
                            }
                            if (z == 2) {
                                b = false;
                            }
                            if (z != 1 && z != 2) {
                                System.out.println("Invalid option. Please try again.");
                            }

                        }

                        return;

                    case 3:
                        projectName = inputHandler.searchProject();
                        projectTemp = edition.getProject(projectName);
                        for (int i = 0; i < projectTemp.getNumberOfTasks(); i++) {
                            System.out.println(((MyProject) projectTemp).toString());
                        }
                        return;

                    case 4:
                        isTaskManagementMenuRunning = false;
                        return;

                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        } catch (NullPointerException | IllegalProjectException ex) {
            System.out.println("Elements not properly initialized.");
            return;
        }
    }

    @Override
    public void display() {
        System.out.println("Welcome to the TASKS menu");
        System.out.println("please pick your option:");
        System.out.println("1 - Add Task to project ");
        System.out.println("2 - Remove Task from Project");
        System.out.println("3 - List a Projects tasks");
        System.out.println("4 - return");
    }
}