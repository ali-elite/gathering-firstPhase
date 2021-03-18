package ir.sharif.ap2021.View.Menus.ExploreThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import ir.sharif.ap2021.View.Menus.TimeLineMenu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static ir.sharif.ap2021.Controller.LogicalAgent.userDirectory;

public class ExploreTimeLine extends TimeLineMenu {


    public ExploreTimeLine(Menu parentMenu) {
        super(parentMenu);
        this.setName("Popular Thoughts");
    }

    @Override
    public void show() {
        thoughts = new ArrayList<>();
        for (File f : Objects.requireNonNull(userDirectory.listFiles())) {

            int i = 0;

            String str = "";
            try {
                str = FileUtils.readFileToString(f);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            User theUser = gson.fromJson(str, User.class);

            if (!theUser.getThoughts().isEmpty()) {
                thoughts.add(theUser.getThoughts().get(0).getId());
            }


            i++;
            if (i == 20) {
                this.parentMenu.show();
                this.parentMenu.execute();
            }


        }




        //////.............................////

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "PopularPage" + ":" + ConsoleColors.RESET);

        if (thoughts.size() == 0) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Your PopularPage is Empty! " + ConsoleColors.RESET);
            this.parentMenu.show();
            this.parentMenu.execute();
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
}
