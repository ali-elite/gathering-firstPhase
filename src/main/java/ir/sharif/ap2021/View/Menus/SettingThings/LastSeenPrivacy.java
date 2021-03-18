package ir.sharif.ap2021.View.Menus.SettingThings;

import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;

import java.util.HashMap;

public class LastSeenPrivacy extends Menu {

    public LastSeenPrivacy(Menu parentMenu) {
        super("Last Seen Privacy", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getLsPublic());
        submenus.put(2, getLsPrivate());
        submenus.put(3, getLsSemiPrivate());

        this.setSubmenus(submenus);
    }

    protected Menu getLsPublic() {
        return new Menu("Set LastSeen to Show For EveryOne", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                user.setLSPublic();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Your LastSeen Has Set Public Successfully!" + ConsoleColors.RESET);


                user.saveUser();
                Menu.setUser(user.loadUser());
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }

    protected Menu getLsPrivate() {
        return new Menu("Set LastSeen to Show For NoOne", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                user.setLSPrivate();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Your LastSeen Has Set Private Successfully!" + ConsoleColors.RESET);


                user.saveUser();
                Menu.setUser(user.loadUser());
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }

    protected Menu getLsSemiPrivate() {
        return new Menu("Set LastSeen to Show Just For Followers", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                user.setLSSemiPrivate();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Your LastSeen Has Set SemiPrivate Successfully!" + ConsoleColors.RESET);


                user.saveUser();
                Menu.setUser(user.loadUser());
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }


}
