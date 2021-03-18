package ir.sharif.ap2021.View.Menus.PersonalThings;


import com.google.gson.Gson;
import ir.sharif.ap2021.Model.News.RequestThings.Request;
import ir.sharif.ap2021.Model.News.SystemMessageThings.SystemMessage;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Requests extends Menu {

    public Request request;

    public Requests(Menu parentMenu) {
        super("Requests", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getAcceptReq());
        submenus.put(2, getSilentRejectReq());
        submenus.put(3, getReject());
        this.setSubmenus(submenus);
    }

    @Override
    public void show() {

        if (user.getRequests().isEmpty()) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "No Request."
                    + ConsoleColors.RESET);

            this.parentMenu.show();
            this.parentMenu.execute();

        } else {
            for (int i = 0; i < user.getRequests().size(); i++) {

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "***" + i + ":  "
                        + ConsoleColors.RESET);

                user.getRequests().get(i).view();
            }

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Choose The Number of The Request that you want to respond"
                    + ConsoleColors.RESET);


            String answer = userAns(scanner);
            while (!validReqOption(answer)) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);
            }

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "So Your Chosen Request is :"
                    + ConsoleColors.RESET);

            user.getRequests().get(Integer.parseInt(answer)).view();

            this.request = user.getRequests().get(Integer.parseInt(answer));

            for (Integer menuNum : submenus.keySet()) {
                System.out.println(menuNum + ". " + submenus.get(menuNum).getName());
            }
            if (this.parentMenu != null)
                System.out.println((submenus.size() + 1) + ". Back");
            else {
                System.out.println((submenus.size() + 1) + ". Exit");
            }
        }
    }

    protected Menu getAcceptReq() {
        return new Menu("Accepting Request", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                User reSender = findUser(request.getSender());

                user.getFollowers().add(request.getSender());
                reSender.getFollowings().add(user.getId());

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Request Accepted!" + ConsoleColors.RESET);

                user.getSystemMessages().add(new SystemMessage(user, findUser(request.getSender()).getUserName() + " followed you!"));
                reSender.getSystemMessages().add(new SystemMessage(reSender, "you have followed " + user.getUserName()));

                user.getRequests().removeIf(rq -> rq.getId() == request.getId());

                user.saveUser();
                user = user.loadUser();

                reSender.saveUser();

                this.parentMenu.show();
                this.parentMenu.execute();

            }

        };
    }

    protected Menu getSilentRejectReq() {
        return new Menu("Silent Rejecting Request", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                user.getRequests().removeIf(rq -> rq.getId() == request.getId());
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Request Rejected Silently!" + ConsoleColors.RESET);

                user.saveUser();

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getReject() {
        return new Menu("Rejecting Request", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
            }

            @Override
            public void execute() {

                User reSender = findUser(request.getSender());

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + " Request Rejected!" + ConsoleColors.RESET);

                user.getSystemMessages().add(new SystemMessage(user, "you denied " +reSender.getUserName()));
                reSender.getSystemMessages().add(new SystemMessage(reSender, user.getUserName() + " has denied you"));


                user.getRequests().removeIf(rq -> rq.getId() == request.getId());


                user.saveUser();
                reSender.saveUser();

                user = user.loadUser();
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

    public static boolean validReqOption(String s) {

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }

        return Integer.parseInt(s) < user.getRequests().size();
    }


}


