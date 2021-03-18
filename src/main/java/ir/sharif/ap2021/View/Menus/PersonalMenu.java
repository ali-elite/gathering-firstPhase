package ir.sharif.ap2021.View.Menus;

import com.google.gson.Gson;
import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.Model.ThoughtThings.Thought;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.PersonalThings.EditProfile;
import ir.sharif.ap2021.View.Menus.PersonalThings.Followers;
import ir.sharif.ap2021.View.Menus.PersonalThings.Followings;
import ir.sharif.ap2021.View.Menus.PersonalThings.News;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class PersonalMenu extends Menu {


    public PersonalMenu(Menu parentMenu) {
        super("Personal Page", parentMenu);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getNewThought());
        submenus.put(2, getThoughtHistory());
        submenus.put(3, new EditProfile(this));
        submenus.put(4, new Followers(this));
        submenus.put(5, new Followings(this));
        submenus.put(6, getBlackList());
        submenus.put(7, getInfo());
        submenus.put(8, new News(this));
        this.setSubmenus(submenus);
    }

    protected Menu getNewThought() {
        return new Menu("New Thought", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Write what is in your mind(at most 140 characters):"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);

                while (!isValidTweet(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your thought is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                String text = answer;
                Thought thought = new Thought("t", user, null, text, LocalDateTime.now());

                thought.saveThought();
                thought = thought.loadThought();


                user.getThoughts().add(thought);


                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Thought has been Created Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getThoughtHistory() {
        return new Menu("Your Thoughts History", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                for (int i = 0; i < user.getThoughts().size(); i++) {
                    user.getThoughts().get(i).view();
                }

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getBlackList() {
        return new Menu("Black List", this) {
            @Override
            public void show() {

                if (user.getBlackList().isEmpty()) {
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You Blocked No One."
                            + ConsoleColors.RESET);


                    this.parentMenu.show();
                    this.parentMenu.execute();
                } else {
                    System.out.println(this.getName() + ":");
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You blocked these people :"
                            + ConsoleColors.RESET);

                    for (int i = 0; i < user.getBlackList().size(); i++) {

                        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "* " + findUser(user.getBlackList().get(i)).getUserName()
                                + ConsoleColors.RESET);

                    }

                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Type any username you want to unblock :"
                            + ConsoleColors.RESET);
                }
            }

            @Override
            public void execute() {

                String userCheck = userAns(scanner);

                while (!blockedExistence(userCheck)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "user does not exists" + "\n" + "try again" + ConsoleColors.RESET);
                    userCheck = userAns(scanner);
                }

                String str = "";
                try {
                    str = FileUtils.readFileToString(new File(LogicalAgent.userDirectory + "/" + userCheck + ".txt"));

                } catch (IOException e) {
                    e.printStackTrace();
                }


                Gson gson = new Gson();
                User blocked = gson.fromJson(str, User.class);
                user.getBlackList().remove((Integer) blocked.getId());
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Unblocked Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();

            }
        };
    }

    protected Menu getInfo() {
        return new Menu("Info", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Here is your profile :"
                        + ConsoleColors.RESET);

            }

            @Override
            public void execute() {

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT +
                        "firstname :" + user.getFirstName() + "\n" +
                        "lastname :" + user.getLastName() + "\n" +
                        "username :" + user.getUserName() + "\n" +
                        "birthday :" + user.getBirthday() + "\n" +
                        "biography :" + user.getBiography() + "\n" +
                        "emailAddress :" + user.getEmail() + "\n" +
                        "phoneNumber :" + user.getPhoneNumber() + "\n" +
                        "---------"
                        + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }


    public static boolean isValidTweet(String s) {
        return s.length() <= 140;
    }

    public static User findUser(int ID) {


        File us = new File("./USERS/");

        for (File f : Objects.requireNonNull(us.listFiles())) {

            String str = "";
            try {
                str = FileUtils.readFileToString(f);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            User fUser = gson.fromJson(str, User.class);

            if (fUser.getId() == ID) {
                return fUser;
            }

        }
        return null;
    }

    public boolean blockedExistence(String s) {
        for (int i = 0; i < user.getBlackList().size(); i++) {
            if (findUser(user.getBlackList().get(i)).getUserName().equals(s)) {
                return true;
            }
        }
        return false;
    }
}




























