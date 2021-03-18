package ir.sharif.ap2021.Model.ChatThings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.sharif.ap2021.Model.ThoughtThings.Thought;
import ir.sharif.ap2021.Model.UserThings.User;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Objects;

public class Chat extends Thought {


    private boolean seen = false;


    public Chat(String type, User user, User doed, String text, LocalDateTime localDateTime) {

        this.type = type;
        this.localDateTime = localDateTime;
        this.user = user.getId();
        if (type.equalsIgnoreCase("t")) {
            this.doed = 0;
        } else this.doed = doed.getId();
        this.text = text;
//        String cAddress = "./CHATS/";
//        this.id = Integer.parseInt(user.getId() + String.valueOf(doed.getId()));
//
//
//
//        Gson gson = new Gson();
//        String json = gson.toJson(this);
//
//
//        File ChatFile = new File("./CHATS/" + id + ".txt");
//        try {
//            ChatFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        try (PrintStream ps = new PrintStream(ChatFile)) {
//            ps.print(json);
//            ps.close();
//            ps.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }


    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

//    public Chat loadChat() {
//
//        File ThoughtFile = new File("./CHATS/" + id + ".txt");
//
//        String str = "";
//        try {
//            str = FileUtils.readFileToString(ThoughtFile);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Gson gson = new Gson();
//        Chat thought = gson.fromJson(str, Chat.class);
//
//        return thought;
//
//    }
//
//    public void saveChat() {
//
//        File ThoughtFile = new File("./CHATS/" + id + ".txt");
//
//
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        String json = gson.toJson(this);
//
//        try (PrintStream ps = new PrintStream(ThoughtFile)) {
//            ps.print(json);
//            ps.close();
//            ps.flush();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }


}
