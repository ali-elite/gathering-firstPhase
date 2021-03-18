package ir.sharif.ap2021.View.Menus;

import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public abstract class Menu {
    private String name;
    protected HashMap<Integer, Menu> submenus;
    protected Menu parentMenu;
    public static Scanner scanner;
    protected static User user;
    protected static ArrayList<Menu> allMenus;

    static {
        allMenus = new ArrayList<>();
    }

    public static void setScanner(Scanner scanner) {
        Menu.scanner = scanner;
    }

    public static void setUser(User user) {
        Menu.user = user;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public Menu(String name, Menu parentMenu) {
        this.name = name;
        this.parentMenu = parentMenu;
        allMenus.add(this);
    }

    public String getName() {
        return name;
    }

    public void setSubmenus(HashMap<Integer, Menu> submenus) {
        this.submenus = submenus;
    }


    public void show() {
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + this.name + ":" + ConsoleColors.RESET);
        for (Integer menuNum : submenus.keySet()) {
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + menuNum + ". " + submenus.get(menuNum).getName() + ConsoleColors.RESET);
        }
        if (this.parentMenu != null)
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + (submenus.size() + 1) + ". Back" + ConsoleColors.RESET);
        else
            System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + (submenus.size() + 1) + ". Exit" + ConsoleColors.RESET);
    }


    public void execute() {
        Menu nextMenu = null;

//        int chosenMenu = Integer.parseInt(scanner.nextLine());
        String chosenMenuString = userAns(scanner);

        while (!validOption(chosenMenuString)) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
            chosenMenuString = userAns(scanner);
        }

        int chosenMenu = Integer.parseInt(chosenMenuString);
        if (chosenMenu == submenus.size() + 1) {
            if (this.parentMenu == null)
                System.exit(1);
            else
                nextMenu = this.parentMenu;
        } else
            nextMenu = submenus.get(chosenMenu);

        user.saveUser();


        nextMenu.setUser(user.loadUser());
        nextMenu.show();
        nextMenu.execute();
    }


    public static String userAns(Scanner scanner) {

        String s = scanner.nextLine();
        if (s.equals("exit")) {
            user.saveUser();
            System.out.println("bye!");
            System.exit(0);
        }
        return s;
    }

    public boolean validOption(String s) {

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }

        return Integer.parseInt(s) <= submenus.size() + 1 ;
    }
}
