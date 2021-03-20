package ir.sharif.ap2021.View.Menus;

import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.SettingThings.PrivacyMenu;


import java.util.HashMap;

public class SettingMenu extends Menu {
    public SettingMenu(Menu parentMenu) {
        super("Setting", parentMenu);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, new PrivacyMenu(this));
        submenus.put(2, getDeleteUser());
        submenus.put(3, getLogOut());
        this.setSubmenus(submenus);
    }


    protected Menu getDeleteUser() {
        return new Menu("Deleting User", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                logger.info("user " + user.getId() + " deleted by order of the user");
                user.getUserFile().delete();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Deleted Successfully! Hope To see you again here *tears*" + ConsoleColors.RESET);


                LogicalAgent logicalAgent = new LogicalAgent();
                logicalAgent.run();

            }
        };
    }

    protected Menu getLogOut() {
        return new Menu("Logging Out", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "See You Later"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                logger.info("user " + user.getId() + " logged out from the app");
                LogicalAgent logicalAgent = new LogicalAgent();
                logicalAgent.run();

            }
        };
    }


}
