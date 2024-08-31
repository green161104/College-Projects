import userInteraction.menus.MenuManager;
import userInteraction.menus.MenuStartManagement;


public class demo {
    public static void main(String[] args) {
        MenuManager menu = new MenuManager();
        MenuStartManagement.handleStartMenu(menu);
    }

}

