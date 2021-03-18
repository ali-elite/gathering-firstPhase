package ir.sharif.ap2021.Model.News.SystemMessageThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.Model.UserThings.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SystemMessage {

int user;
String text;

    public SystemMessage(User user, String text) {
        this.user = user.getId();
        this.text = text;
    }

    public void view(){
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT +"This is a message for "+ Objects.requireNonNull(findUser(user)).getUserName()+" :"+ "\n" +
                text + "\n" + "--------"
                + ConsoleColors.RESET);

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
