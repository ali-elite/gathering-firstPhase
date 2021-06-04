package ir.sharif.ap2021.View.Menus.PersonalThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.News.RequestThings.Request;
import ir.sharif.ap2021.Model.News.SystemMessageThings.SystemMessage;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Followings extends Menu {

    public User following;
    public ChatScreen chatScreen;

    public Followings(Menu parentMenu) {
        super("Followings", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getMessage());
        submenus.put(2, getFollow());
        submenus.put(3, getMute());
        submenus.put(4, getBlock());
        submenus.put(5, getReport());
        this.setSubmenus(submenus);
    }


    @Override
    public void show() {

        if (user.getFollowings().isEmpty()) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "no following."
                    + ConsoleColors.RESET);

            this.parentMenu.show();
            this.parentMenu.execute();
        } else {

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You are Following these people :"
                    + ConsoleColors.RESET);

            for (int i = 0; i < user.getFollowings().size(); i++) {

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "* " + findUser(user.getFollowings().get(i)).getUserName()
                        + ConsoleColors.RESET);

            }

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Type any username you want to see his/her profile('Back' for Previous Menu) :"
                    + ConsoleColors.RESET);
        }
        String userCheck = userAns(scanner);
        if (userCheck.equalsIgnoreCase("back")) {
            this.parentMenu.show();
            this.parentMenu.execute();
        }

        while (!followingExistence(userCheck)) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "user does not exists" + "\n" + "try again" + ConsoleColors.RESET);
            userCheck = userAns(scanner);
            if (userCheck.equalsIgnoreCase("back")) {
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        }


        String str = "";
        try {
            str = FileUtils.readFileToString(new File(LogicalAgent.userDirectory + "/" + userCheck + ".txt"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        this.following = gson.fromJson(str, User.class);
        this.chatScreen = findChatScreen();

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You Wanted to visit " + following.getUserName() + " Profile!"
                + ConsoleColors.RESET);


        if (!following.isActive()) {
            System.out.println(ir.sharif.ap2021.View.ConsoleColors.RED_BOLD_BRIGHT + "User is deactivated" + "\n" + "returning to the personal page" + ir.sharif.ap2021.View.ConsoleColors.RESET);
            this.parentMenu.show();
            this.parentMenu.execute();
        }

        if(following.getBlackList().contains(user.getId())){
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry This User Has Blocked You!"+ ConsoleColors.RESET);
            this.parentMenu.show();
            this.parentMenu.execute();
        }

        String followShip = "";
        boolean isFollow = user.getFollowers().contains(this.following.getId());
        if (isFollow) {
            followShip = "FOLLOWED YOU";
        } else {
            followShip = "Not FOLLOWING YOU";
        }


        System.out.println(ir.sharif.ap2021.View.ConsoleColors.YELLOW_BOLD_BRIGHT + "You are observing a profile :"
                + ir.sharif.ap2021.View.ConsoleColors.RESET);

        String followingLastSeen = "Recently";
        if (user.getId() == following.getId()) {
            followingLastSeen = following.getLastSeen().toString();
        } else {
            if (user.getLastSeenPrivacy().equalsIgnoreCase("Private")) {
                followingLastSeen = "Last Seen Recently";
            }


            if (user.getLastSeenPrivacy().equalsIgnoreCase("SemiPrivate")) {
                if (user.getFollowers().contains(following.getId())) {
                    followingLastSeen = following.getLastSeen().toString();
                } else {
                    followingLastSeen = "Last Seen Recently";
                }
            }


            if (user.getLastSeenPrivacy().equalsIgnoreCase("Public")) {
                if (this.following.getLastSeenPrivacy().equalsIgnoreCase("Public")) {
                    followingLastSeen = this.following.getLastSeen().toString();
                }
                if (this.following.getLastSeenPrivacy().equalsIgnoreCase("Private")) {
                    followingLastSeen = "Last Seen Recently";
                }
                if (this.following.getLastSeenPrivacy().equalsIgnoreCase("SemiPrivate")) {
                    if (this.following.getFollowers().contains(user.getId())) {
                        followingLastSeen = this.following.getLastSeen().toString();
                    } else {
                        followingLastSeen = "Last Seen Recently";
                    }
                }
            }
        }

        System.out.println(ir.sharif.ap2021.View.ConsoleColors.YELLOW_BOLD_BRIGHT +
                "firstname :" + this.following.getFirstName() + "\n" +
                "lastname :" + this.following.getLastName() + "\n" +
                "username :" + this.following.getUserName() + "\n" +
                "lastSeen :" + followingLastSeen + "\n" +
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

    protected Menu getMessage() {
        return new Menu("Message", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                if (!user.getFollowings().contains(following.getId())) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You aren't Following This User. Follow him/her first" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
            }

            @Override
            public void execute() {

                chatScreen = findChatScreen();

                if (chatScreen == null) {
                    chatScreen = new ChatScreen(user.getId(), following.getId());
                    chatScreen.getChats().add(new Chat("c", user, following, "Conversation Started", LocalDateTime.now()));
                    user.getChatScreens().add(chatScreen);
                    following.getChatScreens().add(chatScreen);
                }


                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Message :"
                        + ConsoleColors.RESET);


                String answer;
                answer = userAns(scanner);

                while (!isValidMessage(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your thought is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    answer = userAns(scanner);
                }

                String text = answer;
                Chat chat = new Chat("c", user, following, text, LocalDateTime.now());


                user.getChatScreens().get(user.getChatScreens().indexOf(chatScreen)).getChats().add(chat);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Sent Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                following.saveUser();
                following = following.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getFollow() {
        return new Menu("Follow(or Unfollow if already Followed)", this) {
            @Override
            public void show() {
                if (following.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Follow Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                //user --> clicker
                //this --> follower

                if (user.getFollowings().contains(following.getId())) {
                    user.getFollowings().remove((Integer)following.getId());
                    following.getFollowers().remove((Integer)user.getId());
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User unfollowed Successfully!" + ConsoleColors.RESET);

                    following.getSystemMessages().add(new SystemMessage(following, user.getUserName() + " unfollowed you!"));
                    user.getSystemMessages().add(new SystemMessage(user, "you have unfollowed " + following.getUserName()));

                } else {
                    if (following.isPrivate()) {

                        for(Request req : following.getRequests()){
                            if(req.getSender() == user.getId()) {
                                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Already Sent a Request." + ConsoleColors.RESET);
                                this.parentMenu.show();
                                this.parentMenu.execute();
                            }
                        }


                        following.getRequests().add(new Request(user, following));
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Request has Sent Successfully!" + ConsoleColors.RESET);

                    } else {
                        user.getFollowings().add(following.getId());
                        following.getFollowers().add(user.getId());
                        following.getSystemMessages().add(new SystemMessage(following, user.getUserName() + " followed you!"));
                        user.getSystemMessages().add(new SystemMessage(user, "you have followed " + following.getUserName()));
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User followed Successfully!" + ConsoleColors.RESET);
                    }
                }

                user.saveUser();
                user = user.loadUser();
                following.saveUser();
                following = following.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getBlock() {
        return new Menu("Block(or Unblock if already Blocked)", this) {
            @Override
            public void show() {

                if(following.getId() == user.getId()){
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Block Yourself"+ ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }

                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                //user --> clicker
                //this --> follower

                if (user.getBlackList().contains(following.getId())) {
                    user.getBlackList().remove(user.getBlackList().indexOf(following.getId()));
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User unblocked Successfully!" + ConsoleColors.RESET);
                } else {
                    user.getBlackList().add(following.getId());
                    if (user.getFollowers().contains(following.getId())) {
                        user.getFollowers().remove(user.getFollowers().indexOf(following.getId()));
                    }
                    if (user.getFollowings().contains(following.getId())) {
                        user.getFollowings().remove(user.getFollowings().indexOf(following.getId()));

                    }
                    if (following.getFollowers().contains(user.getId())) {
                        following.getFollowers().remove(following.getFollowers().indexOf(user.getId()));
                    }
                    if (following.getFollowings().contains(user.getId())) {
                        following.getFollowings().remove(following.getFollowings().indexOf(user.getId()));
                    }
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User blocked Successfully!" + ConsoleColors.RESET);
                }

                user.saveUser();
                user = user.loadUser();
                following.saveUser();
                following = following.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getMute() {
        return new Menu("Mute(or Unmute if already Mute)", this) {
            @Override
            public void show() {
                if (following.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Mute Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (user.getMuteList().contains(following.getId())) {
                    user.getMuteList().remove(user.getMuteList().indexOf(following.getId()));
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Unmuted Successfully!" + ConsoleColors.RESET);
                } else {
                    user.getMuteList().add(following.getId());
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Muted Successfully!" + ConsoleColors.RESET);
                }

                user.saveUser();
                user = user.loadUser();
                following.saveUser();
                following = following.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getReport() {
        return new Menu("Report", this) {
            @Override
            public void show() {
                if (following.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Report Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                following.addReport();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User reported Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                following.saveUser();
                following = following.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }


    public static String userAns(Scanner scanner) {

        String s = scanner.nextLine();
        if (s.equals("exit")) {
            user.saveUser();
            System.out.println("bye!");
            System.exit(0);
        }
        return s;
    }

    public boolean followingExistence(String s) {
        for (int i = 0; i < user.getFollowings().size(); i++) {
            if (findUser(user.getFollowings().get(i)).getUserName().equals(s)) {
                return true;
            }
        }
        return false;
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
                logger.info("user " +ID + " file loaded");
                return fUser;
            }

        }
        return null;
    }

    public ChatScreen findChatScreen() {

        for (ChatScreen chatScreen : user.getChatScreens()) {

            if (chatScreen.getId() == Integer.parseInt(user.getId() + String.valueOf(following.getId()))) {
                return chatScreen;
            }
            if (chatScreen.getId() == Integer.parseInt(following.getId() + String.valueOf(user.getId()))) {
                return chatScreen;
            }

        }

        for (ChatScreen chatScreen : following.getChatScreens()) {

            if (chatScreen.getId() == Integer.parseInt(user + String.valueOf(following))) {
                return chatScreen;
            }
            if (chatScreen.getId() == Integer.parseInt(following + String.valueOf(user))) {
                return chatScreen;
            }

        }

        return null;
    }

    public static boolean isValidMessage(String s) {
        return s.length() <= 300;
    }

}
