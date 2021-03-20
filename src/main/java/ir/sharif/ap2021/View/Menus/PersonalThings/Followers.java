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


public class Followers extends Menu {

    public User follower;
    public ChatScreen chatScreen;

    public Followers(Menu parentMenu) {
        super("Followers", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getMessage());
        submenus.put(2, getFollow());
        submenus.put(3, getBlock());
        submenus.put(4, getMute());
        submenus.put(5, getReport());
        this.setSubmenus(submenus);
    }


    @Override
    public void show() {

        if (user.getFollowers().isEmpty()) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "no followers."
                    + ConsoleColors.RESET);

            this.parentMenu.show();
            this.parentMenu.execute();
        } else {

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "These People followed you :"
                    + ConsoleColors.RESET);

            for (int i = 0; i < user.getFollowers().size(); i++) {

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "* " + findUser(user.getFollowers().get(i)).getUserName()
                        + ConsoleColors.RESET);

            }

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Type any username you want to see his/her profile('Back' for Previous Menu)) :"
                    + ConsoleColors.RESET);
        }

        String userCheck = userAns(scanner);
        if (userCheck.equalsIgnoreCase("back")) {
            this.parentMenu.show();
            this.parentMenu.execute();
        }

        while (!followerExistence(userCheck)) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "user does not exists" + "\n" + "try again" + ConsoleColors.RESET);
            logger.error("user " + user.getId() + "wanted to choose a follower who doesn't exists");
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
        this.follower = gson.fromJson(str, User.class);


        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "You Wanted to visit " + follower.getUserName() + " Profile!"
                + ConsoleColors.RESET);


        if (!follower.isActive()) {
            System.out.println(ir.sharif.ap2021.View.ConsoleColors.RED_BOLD_BRIGHT + "User is deactivated" + "\n" + "returning to the personal page" + ir.sharif.ap2021.View.ConsoleColors.RESET);
            logger.info("user " + user.getId() + "choose to visit a deactive user profile");

            this.parentMenu.show();
            this.parentMenu.execute();
        }

        if (follower.getBlackList().contains(user.getId())) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry This User Has Blocked You!" + ConsoleColors.RESET);
            logger.info("user " + user.getId() + "choose to visit a deactive user profile");
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

        for (
                Integer menuNum : submenus.keySet()) {
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

                if (!user.getFollowings().contains(follower.getId())) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You aren't Following This User. Follow him/her first" + ConsoleColors.RESET);
                    logger.info("user " + user.getId() + " was trying to message a non following user");
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }

            }

            @Override
            public void execute() {

                chatScreen = findChatScreen();

                if (chatScreen == null) {
                    chatScreen = new ChatScreen(user.getId(), follower.getId());
                    chatScreen.getChats().add(new Chat("c", user, follower, "Conversation Started", LocalDateTime.now()));
                    user.getChatScreens().add(chatScreen);
                    follower.getChatScreens().add(chatScreen);
                }


                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Message :"
                        + ConsoleColors.RESET);


                String answer;
                answer = userAns(scanner);

                while (!isValidMessage(answer)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "your thought is too long to share." + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " was creating a long thought to share");
                    answer = userAns(scanner);
                }

                String text = answer;
                Chat chat = new Chat("c", user, follower, text, LocalDateTime.now());


                user.getChatScreens().get(user.getChatScreens().indexOf(chatScreen)).getChats().add(chat);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Message Sent Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                follower.saveUser();
                follower = follower.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getFollow() {
        return new Menu("Follow(or Unfollow if already Followed)", this) {
            @Override
            public void show() {

                if (follower.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Follow Yourself" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " wanted to follow him/her self");
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }

                if (user.getBlackList().contains(follower.getId())) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Have Blocked This User. Unblock him/her First" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " wanted to follow a blocked user");
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }


                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (user.getFollowings().contains(follower.getId())) {
                    user.getFollowings().remove(user.getFollowings().indexOf(follower.getId()));
                    follower.getFollowers().remove(follower.getFollowers().indexOf(user.getId()));
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User unfollowed Successfully!" + ConsoleColors.RESET);


                    follower.getSystemMessages().add(new SystemMessage(follower, user.getUserName() + " unfollowed you!"));
                    user.getSystemMessages().add(new SystemMessage(user, "you have unfollowed " + follower.getUserName()));

                } else {
                    if (follower.isPrivate()) {

                        for (Request req : follower.getRequests()) {
                            if (req.getSender() == user.getId()) {
                                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Already Sent a Request." + ConsoleColors.RESET);
                                this.parentMenu.show();
                                this.parentMenu.execute();
                            }
                        }


                        follower.getRequests().add(new Request(user, follower));
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Request has Sent Successfully!" + ConsoleColors.RESET);

                    } else {
                        user.getFollowings().add(follower.getId());
                        follower.getFollowers().add(user.getId());
                        follower.getSystemMessages().add(new SystemMessage(follower, user.getUserName() + " followed you!"));
                        user.getSystemMessages().add(new SystemMessage(user, "you have followed " + follower.getUserName()));
                        System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User followed Successfully!" + ConsoleColors.RESET);
                    }
                }

                user.saveUser();
                user = user.loadUser();
                follower.saveUser();
                follower = follower.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getBlock() {
        return new Menu("Block(or Unblock if already Blocked)", this) {
            @Override
            public void show() {

                if (follower.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Block Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }

                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                //user --> clicker
                //this --> follower

                if (user.getBlackList().contains(follower.getId())) {
                    user.getBlackList().remove(user.getBlackList().indexOf(follower.getId()));
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User unblocked Successfully!" + ConsoleColors.RESET);
                } else {
                    user.getBlackList().add(follower.getId());
                    if (user.getFollowers().contains(follower.getId())) {
                        user.getFollowers().remove(user.getFollowers().indexOf(follower.getId()));
                    }
                    if (user.getFollowings().contains(follower.getId())) {
                        user.getFollowings().remove(user.getFollowings().indexOf(follower.getId()));

                    }
                    if (follower.getFollowers().contains(user.getId())) {
                        follower.getFollowers().remove(follower.getFollowers().indexOf(user.getId()));
                    }
                    if (follower.getFollowings().contains(user.getId())) {
                        follower.getFollowings().remove(follower.getFollowings().indexOf(user.getId()));
                    }
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User blocked Successfully!" + ConsoleColors.RESET);
                }

                user.saveUser();
                user = user.loadUser();
                follower.saveUser();
                follower = follower.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getMute() {
        return new Menu("Mute(or Unmute if already Mute)", this) {
            @Override
            public void show() {
                if (follower.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Mute Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                if (user.getMuteList().contains(follower.getId())) {
                    user.getMuteList().remove(user.getMuteList().indexOf(follower.getId()));
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Unmuted Successfully!" + ConsoleColors.RESET);
                } else {
                    user.getMuteList().add(follower.getId());
                    System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User Muted Successfully!" + ConsoleColors.RESET);
                }

                user.saveUser();
                user = user.loadUser();
                follower.saveUser();
                follower = follower.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getReport() {
        return new Menu("Report", this) {
            @Override
            public void show() {
                if (follower.getId() == user.getId()) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "You Cant Report Yourself" + ConsoleColors.RESET);
                    this.parentMenu.show();
                    this.parentMenu.execute();
                }
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                follower.addReport();
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "User reported Successfully!" + ConsoleColors.RESET);


                user.saveUser();
                user = user.loadUser();
                follower.saveUser();
                follower = follower.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }


    public boolean followerExistence(String s) {
        for (int i = 0; i < user.getFollowers().size(); i++) {
            if (findUser(user.getFollowers().get(i)).getUserName().equals(s)) {
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

        String s1 = String.valueOf(user.getId());
        String s2 = String.valueOf(follower.getId());

        for (ChatScreen ch : user.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        for (ChatScreen ch : follower.getChatScreens()) {

            if (ch.getId() == Integer.parseInt(s1.concat(s2))) {
                return ch;
            }
            if (ch.getId() == Integer.parseInt(s2.concat(s1))) {
                return ch;
            }

        }

        return null;
    }

    public static boolean isValidMessage(String s) {
        return s.length() <= 300;
    }


}
