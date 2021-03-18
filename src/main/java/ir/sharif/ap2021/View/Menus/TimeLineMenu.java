package ir.sharif.ap2021.View.Menus;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.ThoughtThings.Thought;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.ChatBoxThings.PersonalChat;
import ir.sharif.ap2021.View.Menus.TimeLineThings.CommentTimeLine;
import ir.sharif.ap2021.View.Menus.TimeLineThings.UserProfile;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TimeLineMenu extends Menu {


    protected LocalDateTime localDateTime;
    protected ArrayList<Integer> thoughts;
    protected Thought t;
    protected int i = 0;
    protected UserProfile userProfile;


    public TimeLineMenu(Menu parentMenu) {
        super("TimeLine", parentMenu);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getNextThought());
        submenus.put(2, getBackThought());
        submenus.put(3, getSavedMessage());
        submenus.put(4, getForwardMessage());
        submenus.put(5, getMuteAuthor());
        submenus.put(6, getSpam());

        userProfile = new UserProfile(this);
        submenus.put(7, userProfile);

        submenus.put(8, getNewLike());
        submenus.put(9, getNewRethought());
        submenus.put(10, getNewOpinion());


        submenus.put(11, getComments());

        this.setSubmenus(submenus);

        this.localDateTime = LocalDateTime.now();
    }


    @Override
    public void show() {

        ArrayList<Integer> userFollowings = user.getFollowings();
        ArrayList<Integer> userFollowers = user.getFollowers();
        thoughts = new ArrayList<>();


        for (int userFollower : userFollowers) {
            if (!user.getMuteList().contains(userFollower) && User.findUser(userFollower).isActive()) {
                for (int j = 0; j < User.findUser(userFollower).getThoughts().size(); j++) {
                    if (!thoughts.contains(User.findUser(userFollower).getThoughts().get(j).getId())
                            && User.findUser(userFollower).getThoughts().get(j).getLocalDateTime().isAfter(localDateTime.minusDays(1))) {
                        thoughts.add(User.findUser(userFollower).getThoughts().get(j).getId());
                    }
                }
            }

        }


        for (int userFollowing : userFollowings) {
            if (!user.getMuteList().contains(userFollowing) && User.findUser(userFollowing).isActive()) {
                for (int j = 0; j < User.findUser(userFollowing).getThoughts().size(); j++) {
                    if (!thoughts.contains(User.findUser(userFollowing).getThoughts().get(j).getId())
                            && User.findUser(userFollowing).getThoughts().get(j).getLocalDateTime().isAfter(localDateTime.minusDays(1))) {
                        thoughts.add(User.findUser(userFollowing).getThoughts().get(j).getId());
                    }
                }
            }
        }


        for (int j = 0; j < user.getThoughts().size(); j++) {
            if (!thoughts.contains(user.getThoughts().get(j).getId()) &&
                    user.getThoughts().get(j).getLocalDateTime().isAfter(localDateTime.minusDays(1))) {
                thoughts.add(user.getThoughts().get(j).getId());
            }
        }


        //////.............................////

        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "TimeLine" + ":" + ConsoleColors.RESET);

        if (thoughts.size() == 0) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Your TimeLine is Empty! " + ConsoleColors.RESET);
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


    protected Menu getNextThought() {

        return new Menu("Next Thought", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }


            @Override
            public void execute() {
                i++;
                if (i == thoughts.size()) {
                    i--;
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "End of the TimeLine! " + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                    //parentmenu.getparent menu ?
                } else {
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
            }
        };
    }

    protected Menu getBackThought() {

        return new Menu("Previous Thought", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }


            @Override
            public void execute() {
                i--;
                if (i == -1) {
                    i++;
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "No Thought Before!" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                    //parentmenu.getparent menu ?
                } else {
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
            }
        };
    }

    protected Menu getMuteAuthor() {
        return new Menu("Muting The Author", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                if (findUser(t.getUser()).getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Can't Mute Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Author Muted"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                user.getMuteList().add(findUser(t.getUser()).getId());

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getSpam() {
        return new Menu("Reporting Spam", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Thought Reported as Spam!"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                t.addSpam();

                t.saveThought();
                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getNewOpinion() {
        return new Menu("New Opinion", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Write your Opinion about this Thought(at most 140 characters):"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);

                while (!isValidTweet(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your opinion is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                String text = answer;
                Thought opinion = new Thought("o", user, findUser(t.getUser()), text, LocalDateTime.now());

                t.getOpinions().add(opinion.getId());
                user.getThoughts().add(opinion);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Opinion has been Sent Successfully!" + ConsoleColors.RESET);

                t.saveThought();
                t = t.loadThought();
                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getNewLike() {
        return new Menu("Liking the Thought", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (t.getLikers().contains(user.getId())) {
                    t.minusLike();
                    t.getLikers().remove(t.getLikers().indexOf(user.getId()));
                    user.getLikedThoughts().remove(t);

                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Thought Unliked Successfully!" + ConsoleColors.RESET);

                } else {

                    t.addLike();
                    t.getLikers().add(user.getId());
                    user.getLikedThoughts().add(t);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Thought Liked Successfully!" + ConsoleColors.RESET);
                }
                t.saveThought();
                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getSavedMessage() {
        return new Menu("Saving The Thought as a Message", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                Chat c = new Chat("c", user, user, t.getText(), LocalDateTime.now());
                ChatScreen myChatSc;

                if (findSavedMessage() == null) {
                    myChatSc = new ChatScreen(user.getId(), user.getId());
                    user.getChatScreens().add(myChatSc);
                } else {
                    myChatSc = findSavedMessage();
                }

                user.getChatScreens().get(user.getChatScreens().indexOf(myChatSc)).getChats().add(c);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Saved Successfully" + ConsoleColors.RESET);

                user.saveUser();
                Menu.setUser(user.loadUser());


                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getForwardMessage() {
        return new Menu("Forwarding The Thought as a Message", this) {
            @Override
            public void show() {

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

                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter The Username You Want to Forward to:" + ConsoleColors.RESET);

                for (int i = 0; i < chatUsers.size(); i++) {
                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + findUser(chatUsers.get(i)).getUserName() + ConsoleColors.RESET);
                }
            }

            @Override
            public void execute() {

                String answer;
                answer = userAns(scanner);

                User us = findUsername(answer);

                while (us == null) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "User doesn't Exists" + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                if (!isChatScreenExists(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You don't have personal chat with this user. Please Make One First." + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }


                Chat c = new Chat("c", findUser(t.getUser()), us, t.getText(), LocalDateTime.now());
                ChatScreen myCHS = findChatScreen(answer);

                for (ChatScreen ch : user.getChatScreens()) {
                    if (ch.getId() == myCHS.getId()) {
                        ch.getChats().add(c);
                    }
                }

                for (ChatScreen ch : us.getChatScreens()) {
                    if (ch.getId() == myCHS.getId()) {
                        ch.getChats().add(c);
                    }
                }


                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Forwarded Successfully" + ConsoleColors.RESET);

                user.saveUser();
                us.saveUser();

                Menu.setUser(user.loadUser());

                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getNewRethought() {
        return new Menu("Rethoughting the Thought", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (!t.getRethoughters().contains(user.getId())) {
                    t.addRethought();
                    t.getRethoughters().add(user.getId());
                    t.saveThought();

                    user.getThoughts().add(t);
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Thought RETHOUGHTED Successfully!" + ConsoleColors.RESET);
                } else {
                    t.minusRethought();
                    t.getRethoughters().remove(t.getRethoughters().indexOf(user.getId()));
                    t.saveThought();

                    user.getThoughts().removeIf(th -> th.getId() == t.getId());

                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Thought UnRETHOUGHTED Successfully!" + ConsoleColors.RESET);

                }




                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getComments() {
        return new Menu("Opinions", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                CommentTimeLine commentTimeLine = new CommentTimeLine(this);

                if (t.getOpinions().isEmpty()) {

                    System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "No Opinion for this thought" + ConsoleColors.RESET);

                    commentTimeLine.getParentMenu().getParentMenu().show();
                    commentTimeLine.getParentMenu().getParentMenu().execute();
                }

                commentTimeLine.setSelected(t);
                commentTimeLine.show();
                commentTimeLine.execute();


//                t.saveThought();
//                user.saveUser();
//                user = user.loadUser();
//                this.parentMenu.show();
//                this.parentMenu.execute();
            }
        };
    }


    public static boolean isValidTweet(String s) {
        return s.length() <= 140;
    }

    public static Thought findThought(int ID) {


        File us = new File("./THOUGHTS/");

        for (File f : Objects.requireNonNull(us.listFiles())) {

            String str = "";
            try {
                str = FileUtils.readFileToString(f);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Thought t = gson.fromJson(str, Thought.class);

            if (t.getId() == ID) {
                return t;
            }

        }
        return null;
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

    public ChatScreen findSavedMessage() {

        String s1 = String.valueOf(user.getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s1))) {
                return ch;
            }

        }

        return null;
    }

    public ChatScreen findChatScreen(String s) {

        String s1 = String.valueOf(user.getId());
        String s2 = String.valueOf(findUsername(s).getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        for (ChatScreen ch : findUsername(s).getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        return null;
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

    public static boolean isChatScreenExists(String s) {

        String s1 = String.valueOf(user.getId());
        String s2 = String.valueOf(findUsername(s).getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return true;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return true;
            }

        }

        for (ChatScreen ch : findUsername(s).getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return true;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return true;
            }
        }
        return false;
    }
}