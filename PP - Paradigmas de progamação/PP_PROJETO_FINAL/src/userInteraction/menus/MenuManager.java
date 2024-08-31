/**
 * Nome: Sónia Raquel Degtyarëva de Oliveira
 * Número: 8220114
 * Turma: Turma 4
 * <p>
 * Nome: João Pedro da Silva Santos
 * Número: 8220256
 * Turma: Turma 1
 * <p>
 * This class's focus is to handle all menu related operations and the chain of menus
 */

package userInteraction.menus;

import java.util.Scanner;

public class MenuManager {
    private static final int MAX_SIZE = 300; // tamanho do array
    private int currentOption = 0; // index da opçao em q estás
    private MenuDisplay[] menuStack; // é um array de interfaces MenuDisplay, ou seja é um array de implementações do método Display
    private int lastOption = currentOption--; //é a variável que guarda o index no array anterior à currente

    /**
     * Initializes the menu stack, which is what keeps track of the menu displays used
     */
    public MenuManager() {
        menuStack = new MenuDisplay[MAX_SIZE];
    }

    /**
     * Receives the option inputted by the user, and updates the option index
     *
     * @return the chosen option
     */
    public int getInput() {
        int option;
        Scanner scanner = new Scanner(System.in);
        option = scanner.nextInt();
        currentOption++;
        return option;
    }

    /**
     * Method responsible for displaying the correct menu, corresponding to the latest option in the index
     *
     * @param menu
     */
    public void displayMenu(MenuDisplay menu) {
        if (lastOption < MAX_SIZE - 1) {
            menuStack[++currentOption] = menu;
            menu.display();
        } else {
            System.out.println("Menu stack is full. Can't push new menu.");
        }
    }

    /** public void goBack() {

     if (lastOption >= 0) {
     MenuDisplay previousMenu = menuStack[lastOption];
     previousMenu.display();
     } else {
     System.out.println("Cannot go back further. Exiting program.");
     System.exit(0);
     }
     }**/
}
