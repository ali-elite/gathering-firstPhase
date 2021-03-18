package ir.sharif.ap2021.View.Menus.ChatBoxThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class PersonalChat extends Menu {

    User secondUser;
    ChatScreen chS;

    public PersonalChat(Menu parentMenu, User secondUser, int seen) {

        super(secondUser.getUserName() + " ( " + seen + " )", parentMenu);
        this.secondUser = secondUser;

        if (seen == 0) {
            this.setName(secondUser.getUserName());
        }


        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getMakeNewPm());
        submenus.put(2, getChat());
        this.setSubmenus(submenus);

    }

    protected Menu getMakeNewPm() {

        return new Menu("New Message", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Write a Message (At Most 300 Characters):" + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                chS = findChatScreen();

                String answer;
                answer = userAns(scanner);

                while (!isValidMessage(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your thought is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                String text = answer;
                Chat chat = new Chat("c", user, secondUser, text, LocalDateTime.now());


                user.getChatScreens().get(user.getChatScreens().indexOf(chS)).getChats().add(chat);
                secondUser.getChatScreens().get(secondUser.getChatScreens().indexOf(chS)).getChats().add(chat);


                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Sent Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                Menu.setUser(user.loadUser());
                secondUser.saveUser();

                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getChat() {

        return new Menu("Showing Chats", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                chS = findChatScreen();

                for (Chat chat : chS.getChats()) {
                    chat.view();
                    chat.setSeen(true);
                }

                user.saveUser();
                Menu.user = user.loadUser();
                secondUser.saveUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    public static boolean isValidMessage(String s) {
        return s.length() <= 300;
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

    public ChatScreen findChatScreen() {

        String s1 = String.valueOf(user.getId());
        String s2 = String.valueOf(secondUser.getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        for (ChatScreen ch :secondUser.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        return null;
    }

}
