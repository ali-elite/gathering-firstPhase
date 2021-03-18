package ir.sharif.ap2021.Model.ChatThings;

import java.util.ArrayList;

public class ChatScreen {

    private int user1;
    private int user2;
    private ArrayList<Chat> chats;
    private int id;

    public ChatScreen(int user1, int user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.chats = new ArrayList<>();

        String s1 = String.valueOf(user1);
        String s2 = String.valueOf(user2);

        this.id = Integer.parseInt(s1.concat(s2));
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public int getUser1() {
        return user1;
    }

    public int getUser2() {
        return user2;
    }

    public int getId() {
        return id;
    }
}
