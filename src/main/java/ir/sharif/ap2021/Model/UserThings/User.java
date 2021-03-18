package ir.sharif.ap2021.Model.UserThings;

import com.google.gson.Gson;
import ir.sharif.ap2021.Model.ChatThings.Chat;
import ir.sharif.ap2021.Model.ChatThings.ChatScreen;
import ir.sharif.ap2021.Model.News.RequestThings.Request;
import ir.sharif.ap2021.Model.News.SystemMessageThings.SystemMessage;
import ir.sharif.ap2021.Model.ThoughtThings.Thought;
import ir.sharif.ap2021.View.Menus.Menu;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class User {


    public String firstName;
    public String lastName;
    public String userName;
    public String biography;
    private LocalDate birthday;
    private String email;
    private String phoneNumber;
    private String password;
    private String LastSeenPrivacy = "Public";
    private int id;
    private final int nextId = 122;
    private boolean isActive = true;
    private boolean isPrivate = false;
    private long reportedTimes = 0;
    private final LocalDateTime lastSeen;
    private final ArrayList<Thought> likedThoughts;
    private final ArrayList<Thought> thoughts;
    private final ArrayList<Integer> followers;
    private final ArrayList<Integer> followings;
    private final ArrayList<UserCategory> categories;
    private final ArrayList<Integer> blackList;
    private final ArrayList<Integer> muteList;
    private final ArrayList<Request> requests;
    private final ArrayList<SystemMessage> systemMessages;
    private final ArrayList<ChatScreen> chatScreens;
    public String userAddress = "./USERS/";
    private File userFile;


    public User(String firstName, String lastName, String userName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.id = nextId + Objects.requireNonNull(new File(userAddress).listFiles()).length;
        this.lastSeen = LocalDateTime.now();
        likedThoughts = new ArrayList<>();
        thoughts = new ArrayList<>();
        followers = new ArrayList<>();
        followings = new ArrayList<>();
        categories = new ArrayList<>();
        blackList = new ArrayList<>();
        requests = new ArrayList<>();
        muteList = new ArrayList<>();
        systemMessages = new ArrayList<>();
        chatScreens = new ArrayList<>();


        userFile = new File(userAddress + this.userName + ".txt");
        if (!userFile.getParentFile().exists()) userFile.getParentFile().mkdirs();

        try {
            userFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        String json = gson.toJson(this);


        try (PrintStream ps = new PrintStream(userFile)) {
            ps.print(json);
            ps.close();
            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }


    public File getUserFile() {
        return userFile;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ArrayList<ChatScreen> getChatScreens() {
        return chatScreens;
    }

    public ArrayList<UserCategory> getCategories() {
        return categories;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public void setUserFile(File userFile) {
        this.userFile = userFile;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLSPublic() {
        this.LastSeenPrivacy = "Public";
    }

    public void setLSPrivate() {
        this.LastSeenPrivacy = "Private";
    }

    public void setLSSemiPrivate() {
        this.LastSeenPrivacy = "SemiPrivate";
    }

    public String getLastSeenPrivacy() {
        return LastSeenPrivacy;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Thought> getThoughts() {
        return thoughts;
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Integer> getFollowers() {
        return followers;
    }

    public ArrayList<Integer> getFollowings() {
        return followings;
    }

    public ArrayList<Thought> getLikedThoughts() {
        return likedThoughts;
    }

    public ArrayList<Integer> getBlackList() {
        return blackList;
    }

    public boolean isActive() {
        return isActive;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBiography() {
        return biography;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public ArrayList<SystemMessage> getSystemMessages() {
        return systemMessages;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void addReport() {
        reportedTimes++;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public long getReportedTimes() {
        return reportedTimes;
    }

    public void setReportedTimes(long reportedTimes) {
        this.reportedTimes = reportedTimes;
    }

    public ArrayList<Integer> getMuteList() {
        return muteList;
    }


    //save
    public void saveUser() {

        Gson gson = new Gson();
        String userdata = gson.toJson(this);

        File userFile = new File(userAddress + this.userName + ".txt");
        try (PrintStream ps = new PrintStream(userFile)) {
            ps.print(userdata);
            ps.close();
            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    //load

    public static Thought loadThought(File ThoughtFile) {

        String str = "";
        try {
            str = FileUtils.readFileToString(ThoughtFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Thought thought = gson.fromJson(str, Thought.class);

        return thought;

    }


    public User loadUser() {

        File userFile = new File("./USERS/" + this.userName + ".txt");

        String str = "";
        try {
            str = FileUtils.readFileToString(userFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        User user = gson.fromJson(str, User.class);


        return user;
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
