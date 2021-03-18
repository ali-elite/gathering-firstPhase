package ir.sharif.ap2021.Model.UserThings;

import java.util.ArrayList;

public class UserCategory {

    ArrayList<Integer> users;
    String name;

    public UserCategory(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

}
