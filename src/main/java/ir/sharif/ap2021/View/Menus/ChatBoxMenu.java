package ir.sharif.ap2021.View.Menus;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.Model.UserThings.UserCategory;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.ChatBoxThings.MessageBox;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ChatBoxMenu extends Menu {

    public ChatBoxMenu(Menu parentMenu) {
        super("ChatBox", parentMenu);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, new MessageBox(this));
        submenus.put(2, getCategorizePeople());
        this.setSubmenus(submenus);
    }


    protected Menu getCategorizePeople() {

        return new Menu("Making New Categories", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter the Name of the Category ('back' for previous menu): " + ConsoleColors.RESET);

            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);

                while (!SearchCat(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "This name Already Exists!" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }
                String name = answer;
                UserCategory userCategory = new UserCategory(name);

                ArrayList<Integer> chatUsers = new ArrayList<>();
                for (ChatScreen chatScreen : user.getChatScreens()) {
                    int u;
                    if (chatScreen.getChats().get(0).getUser() == user.getId()) {
                        u = chatScreen.getChats().get(0).getDoed();
                        chatUsers.add(u);
                    }
                    if (chatScreen.getChats().get(0).getUser() != user.getId()) {
                        u = chatScreen.getChats().get(0).getUser();
                        chatUsers.add(u);
                    }
                }

                answer = "s";

                while (!answer.equalsIgnoreCase("back")) {

                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter the Username You Wanna Add ('back' for previous menu): " + ConsoleColors.RESET);
                    for (int i : chatUsers) {
                        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "* " + findUser(i).getUserName() + ConsoleColors.RESET);
                    }


                    answer = userAns(scanner);
                    if (answer.equalsIgnoreCase("back")) {
                        this.getParentMenu().show();
                        this.getParentMenu().execute();
                    }


                    while (!chatUsers.contains(findUsername(answer).getId())) {
                        System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "This name doesn't Exists!" + "\n" + "try again" + ConsoleColors.RESET);
                        answer = userAns(scanner);
                        if (answer.equalsIgnoreCase("back")) {
                            this.getParentMenu().show();
                            this.getParentMenu().execute();
                        }
                    }

                    userCategory.getUsers().add(findUsername(answer).getId());
                    chatUsers.remove((Integer) findUsername(answer).getId());

                    if (chatUsers.isEmpty()) {
                        System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "No One available Anymore" + ConsoleColors.RESET);
                        break;
                    }

                }

                user.getCategories().add(userCategory);
                user.saveUser();
                Menu.user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
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

    public static boolean SearchCat(String s) {
        for (UserCategory uc : user.getCategories()) {
            if (uc.getName().equals("s")) {
                return false;
            }

        }

        return true;
    }

    public static User findUsername(String s) {


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

            if (fUser.getUserName().equals(s)) {
                return fUser;
            }

        }
        return null;
    }

}
