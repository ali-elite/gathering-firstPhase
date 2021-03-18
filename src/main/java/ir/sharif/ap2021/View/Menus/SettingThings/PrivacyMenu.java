package ir.sharif.ap2021.View.Menus.SettingThings;

import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;

import java.util.HashMap;

public class PrivacyMenu extends Menu {

    public PrivacyMenu(Menu parentMenu) {
        super("Privacy", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getChangeAccountPrivacy());
        submenus.put(2, new LastSeenPrivacy(this));
        submenus.put(3, getChangeAccountActivity());
        this.setSubmenus(submenus);
    }


    protected Menu getChangeAccountPrivacy() {
        return new Menu("Changing Account Privacy", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "1.Set Account Public" + "\n" + "2.Set Account Private"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);


                while (!isValidOption2(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                if (answer.equals("1")) {
                    user.setPrivate(false);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account set Public Successfully!" + ConsoleColors.RESET);

                }
                if (answer.equals("2")) {
                    user.setPrivate(true);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account set Private Successfully!" + ConsoleColors.RESET);

                }

                user.saveUser();
                Menu.setUser(user.loadUser());
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }


    protected Menu getChangeAccountActivity() {
        return new Menu("Changing Account Activity", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "1.Activate" + "\n" + "2.Deactivate"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);


                while (!isValidOption2(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                if (answer.equals("1")) {
                    user.setActive(true);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account Activated Successfully!" + ConsoleColors.RESET);

                }
                if (answer.equals("2")) {
                    user.setActive(false);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account Deactivated Successfully!" + ConsoleColors.RESET);

                }

                user.saveUser();
                Menu.setUser(user.loadUser());
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }


    public boolean isValidOption2(String s) {
        if (s.equals("1")) {
            return true;
        }
        if (s.equals("2")) {
            return true;
        }

        return false;
    }

}
