package ir.sharif.ap2021.View.Menus;


import com.google.gson.Gson;
import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.ExploreThings.ExploreTimeLine;
import ir.sharif.ap2021.View.Menus.TimeLineThings.UserProfile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static ir.sharif.ap2021.Controller.LogicalAgent.userDirectory;

public class ExploreMenu extends Menu {

    public ExploreMenu(Menu parentMenu) {
        super("Explore", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();

        submenus.put(1, getNameForExplore());
        submenus.put(2, new ExploreTimeLine(this));

        this.setSubmenus(submenus);

    }


    protected Menu getNameForExplore() {
        return new Menu("Searching Menu", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter a Username For Search(Type 'Back' for Previous Menu) :"
                        + ConsoleColors.RESET);

            }

            @Override
            public void execute() {
                LogicalAgent logicalAgent = new LogicalAgent();

                String answer = userAns(scanner);
                if (answer.equalsIgnoreCase("back")) {
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                while (!logicalAgent.fileSearch(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry we do not recognize your username" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                    if (answer.equalsIgnoreCase("back")) {
                        this.parentMenu.show();
                        this.parentMenu.execute();
                    }
                }

                String str = "";
                try {
                    str = FileUtils.readFileToString(new File(userDirectory + "/" + answer + ".txt"));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                User viewed = gson.fromJson(str, User.class);
                viewed = viewed.loadUser();


                UserProfile userProfile = new UserProfile(this);
                userProfile.setSelected(viewed);
                userProfile.show();
                userProfile.execute();
            }
        };
    }


}
