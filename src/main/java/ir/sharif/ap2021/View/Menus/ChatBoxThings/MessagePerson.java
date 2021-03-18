package ir.sharif.ap2021.View.Menus.ChatBoxThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.Menus.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MessagePerson extends Menu {
    public MessagePerson(Menu parentMenu) {
        super("See Contacts For Chat", parentMenu);
    }

    @Override
    public void show() {

        HashMap<Integer, Menu> subMenus = new HashMap<>();
        ArrayList<int[]> chatUsers = new ArrayList<>();

        for (ChatScreen chatScreen : user.getChatScreens()) {
            int[] u = new int[2];
            if (chatScreen.getChats().get(0).getUser() == user.getId() && countSeen(chatScreen.getChats()) != 0) {
                u[0] = chatScreen.getChats().get(0).getDoed();
                u[1] = countSeen(chatScreen.getChats());
                chatUsers.add(u);
            }
            if (chatScreen.getChats().get(0).getUser() != user.getId() && countSeen(chatScreen.getChats()) != 0) {
                u[0] = chatScreen.getChats().get(0).getUser();
                u[1] = countSeen(chatScreen.getChats());
                chatUsers.add(u);
            }
        }

        for (ChatScreen chatScreen : user.getChatScreens()) {
            int[] u = new int[2];
            if (chatScreen.getChats().get(0).getUser() == user.getId() && countSeen(chatScreen.getChats()) == 0) {
                u[0] = chatScreen.getChats().get(0).getDoed();
                u[1] = countSeen(chatScreen.getChats());
                chatUsers.add(u);
            }
            if (chatScreen.getChats().get(0).getUser() != user.getId() && countSeen(chatScreen.getChats()) == 0) {
                u[0] = chatScreen.getChats().get(0).getUser();
                u[1] = countSeen(chatScreen.getChats());
                chatUsers.add(u);
            }
        }


        for (int i = 0; i < chatUsers.size(); i++) {
            subMenus.put(i + 1, new PersonalChat(this, findUser(chatUsers.get(i)[0]),chatUsers.get(i)[1]));
        }

        this.setSubmenus(subMenus);

        super.show();
    }

    public static int countSeen(ArrayList<Chat> c) {
        int count = 0;
        for (Chat ch : c) {
            if (!ch.isSeen()) {
                count++;
            }
        }
        return count;
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
}


