package ir.sharif.ap2021.View.Menus.TimeLineThings;

import ir.sharif.ap2021.Model.ThoughtThings.Thought;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import ir.sharif.ap2021.View.Menus.TimeLineMenu;

import java.util.ArrayList;


public class CommentTimeLine extends TimeLineMenu {

    Thought selected;

    public CommentTimeLine(Menu parentMenu) {
        super(parentMenu);
        this.setName("Observing Opinions");
    }


    public void setSelected(Thought selected) {
        this.selected = selected;
    }


    @Override
    public void show() {

        thoughts = new ArrayList<>();
        thoughts.addAll(selected.getOpinions());

        thoughts.removeIf(thh -> !findUser(findThought(thh).getUser()).isActive());

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "Opinions" + ":" + ConsoleColors.RESET);

        if (thoughts.size() == 0) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Your CommentSection is Empty! " + ConsoleColors.RESET);
            this.parentMenu.getParentMenu().show();
            this.parentMenu.getParentMenu().execute();
        } else {

            t = findThought(thoughts.get(i));
            userProfile.setSelected(findUser(t.getUser()));

            t.view();

            for (Integer menuNum : submenus.keySet()) {
                System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + menuNum + ". " + submenus.get(menuNum).getName() + ConsoleColors.RESET);
            }
            if (this.parentMenu != null)
                System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + (submenus.size() + 1) + ". Back" + ConsoleColors.RESET);
            else
                System.out.println(ConsoleColors.CYAN_BOLD_BRIGHT + (submenus.size() + 1) + ". Exit" + ConsoleColors.RESET);
        }
    }

    @Override
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
                nextMenu = this.parentMenu.getParentMenu();
        } else
            nextMenu = submenus.get(chosenMenu);

        user.saveUser();

        nextMenu.setUser(user.loadUser());
        nextMenu.show();
        nextMenu.execute();
    }
}
