package ir.sharif.ap2021.View.Menus.ChatBoxThings;

import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.Model.UserThings.UserCategory;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;

import java.time.LocalDateTime;
import java.util.HashMap;

public class MessageBox extends Menu {
    public MessageBox(Menu parentMenu) {
        super("MessageBox", parentMenu);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, new MessagePerson(this));
        submenus.put(2, getMessageCategory());
        this.setSubmenus(submenus);
    }


    protected Menu getMessageCategory() {

        return new Menu("Messaging a Category", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Write a Message (At Most 300 Characters):" + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);

                while (!isValidMessage(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your thought is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                String text = answer;

                if (user.getCategories().isEmpty()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "No Category Available" + ConsoleColors.RESET);
                    this.getParentMenu().show();
                    this.getParentMenu().execute();
                }

                for (UserCategory userCategory : user.getCategories()) {
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "* " + userCategory.getName() + ConsoleColors.RESET);
                }


                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Choose your Category:" + ConsoleColors.RESET);

                answer = userAns(scanner);

                while (!catEx(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "No Category with this name" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                UserCategory uc = findCat(answer);

                for (int j : user.getCategories().get(user.getCategories().indexOf(uc)).getUsers()) {

                    User userj = User.findUser(j);
                    Chat chat = new Chat("c", user, userj, text, LocalDateTime.now());
                    ChatScreen chS = findChatScreen(j);


                    user.getChatScreens().get(user.getChatScreens().indexOf(chS)).getChats().add(chat);

                    for (ChatScreen sc : userj.getChatScreens()) {
                        if (sc.getId() == chS.getId()) {
                            sc.getChats().add(chat);
                            userj.saveUser();
                        }
                    }

                }


                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Sent Successfully!" + ConsoleColors.RESET);


                user.saveUser();
                Menu.user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }


    public static boolean isValidMessage(String s) {
        return s.length() <= 300;
    }

    public static boolean catEx(String s) {
        for (UserCategory userCategory : user.getCategories()) {
            if (userCategory.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static UserCategory findCat(String s) {

        for (UserCategory userCategory : user.getCategories()) {
            if (userCategory.getName().equals(s)) {
                return userCategory;
            }
        }
        return null;
    }

    public ChatScreen findChatScreen(int user2ID) {

        String s1 = String.valueOf(user.getId());
        String s2 = String.valueOf(User.findUser(user2ID).getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        for (ChatScreen ch : User.findUser(user2ID).getChatScreens()) {

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
