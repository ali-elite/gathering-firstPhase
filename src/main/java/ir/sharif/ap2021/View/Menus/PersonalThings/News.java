package ir.sharif.ap2021.View.Menus.PersonalThings;

import ir.sharif.ap2021.Model.News.SystemMessageThings.SystemMessage;

import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;

import java.util.HashMap;


public class News extends Menu {
    public News(Menu parentMenu) {
        super("News Section", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getSystemMessages());
        submenus.put(2, new Requests(this));
        this.setSubmenus(submenus);
    }

    protected Menu getSystemMessages() {
        return new Menu("System Messages", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (user.getSystemMessages().isEmpty()) {
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "No System Message."
                            + ConsoleColors.RESET);

                    this.parentMenu.show();
                    this.parentMenu.execute();
                } else {
                    for (SystemMessage systemMessage : user.getSystemMessages()) {
                        systemMessage.view();
                    }

                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
            }
        };
    }


}
