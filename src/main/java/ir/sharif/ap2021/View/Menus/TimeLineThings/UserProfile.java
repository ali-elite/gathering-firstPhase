package ir.sharif.ap2021.View.Menus.TimeLineThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import ir.sharif.ap2021.View.Menus.PersonalThings.Followers;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class UserProfile extends Followers {

    User selected;
    public UserProfile(Menu parentMenu) {
        super(parentMenu);
        this.setName("UserProfile");
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    @Override
    public void show() {

        String str = "";
        try {
            str = FileUtils.readFileToString(new File(LogicalAgent.userDirectory + "/" + selected.getUserName() + ".txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        this.follower = gson.fromJson(str, User.class);

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You Wanted to visit " + follower.getUserName() + " Profile!"
                + ConsoleColors.RESET);


        if (!follower.isActive()) {
            System.out.println(ir.sharif.ap2021.View.ConsoleColors.RED_BOLD_BRIGHT + "User is deactivated" + "\n" + "Try Again" + ir.sharif.ap2021.View.ConsoleColors.RESET);

            this.parentMenu.show();
            this.parentMenu.execute();
        }

        if(follower.getBlackList().contains(user.getId())){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry This User Has Blocked You!"+ ConsoleColors.RESET);
            this.parentMenu.show();
            this.parentMenu.execute();
        }

        String followShip = "";
        boolean isFollow = user.getFollowers().contains(this.follower.getId());
        if (isFollow) {
            followShip = "FOLLOWED YOU";
        } else {
            followShip = "Not FOLLOWING YOU";
        }


        System.out.println(ir.sharif.ap2021.View.ConsoleColors.YELLOW_BOLD_BRIGHT + "You are observing a profile :"
                + ir.sharif.ap2021.View.ConsoleColors.RESET);

        String followerLastSeen = "Recently";
        if (user.getId() == follower.getId()) {
            followerLastSeen = follower.getLastSeen().toString();
        } else {
            if (user.getLastSeenPrivacy().equalsIgnoreCase("Private")) {
                followerLastSeen = "Last Seen Recently";
            }


            if (user.getLastSeenPrivacy().equalsIgnoreCase("SemiPrivate")) {
                if (user.getFollowers().contains(follower.getId())) {
                    followerLastSeen = follower.getLastSeen().toString();
                } else {
                    followerLastSeen = "Last Seen Recently";
                }
            }


            if (user.getLastSeenPrivacy().equalsIgnoreCase("Public")) {
                if (this.follower.getLastSeenPrivacy().equalsIgnoreCase("Public")) {
                    followerLastSeen = this.follower.getLastSeen().toString();
                }
                if (this.follower.getLastSeenPrivacy().equalsIgnoreCase("Private")) {
                    followerLastSeen = "Last Seen Recently";
                }
                if (this.follower.getLastSeenPrivacy().equalsIgnoreCase("SemiPrivate")) {
                    if (this.follower.getFollowers().contains(user.getId())) {
                        followerLastSeen = this.follower.getLastSeen().toString();
                    } else {
                        followerLastSeen = "Last Seen Recently";
                    }
                }
            }
        }

        System.out.println(ir.sharif.ap2021.View.ConsoleColors.YELLOW_BOLD_BRIGHT +
                "firstname :" + this.follower.getFirstName() + "\n" +
                "lastname :" + this.follower.getLastName() + "\n" +
                "username :" + this.follower.getUserName() + "\n" +
                "lastSeen :" + followerLastSeen + "\n" +
                followShip + "\n" + "---------"
                + ir.sharif.ap2021.View.ConsoleColors.RESET);

        for (Integer menuNum : submenus.keySet()) {
            System.out.println(menuNum + ". " + submenus.get(menuNum).getName());
        }
        if (this.parentMenu != null)
            System.out.println((submenus.size() + 1) + ". Back");
        else
            System.out.println((submenus.size() + 1) + ". Exit");
    }
}
