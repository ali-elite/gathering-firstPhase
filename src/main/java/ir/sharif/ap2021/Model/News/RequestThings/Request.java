package ir.sharif.ap2021.Model.News.RequestThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.News.SystemMessageThings.SystemMessage;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.Model.UserThings.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Request {

    private int sender;
    private int recevier;
    private int id;

    public Request(User sender, User recevier) {

        String s1 = String.valueOf(sender.getId());
        String s2 = String.valueOf(recevier.getId());

        this.id = Integer.parseInt(s1.concat(s2));

        this.sender = sender.getId();
        this.recevier = recevier.getId();
    }

    public void view() {

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + findUser(sender).getUserName() + " has sent you a follow request. What should we do?"
                + ConsoleColors.RESET);

    }


    public void reject() {


    }


    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getRecevier() {
        return recevier;
    }

    public void setRecevier(int recevier) {
        this.recevier = recevier;
    }

    public int getId() {
        return id;
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
