package ir.sharif.ap2021.Model.ThoughtThings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.sharif.ap2021.Model.UserThings.User;
import ir.sharif.ap2021.View.ConsoleColors;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


public class Thought {

    protected int user;
    protected int doed;
    protected String type;
    private long likes;
    private long spamReports;
    protected String text;
    private ArrayList<Integer> opinions;
    private ArrayList<Integer> likers;
    private ArrayList<Integer> rethoughters;
    protected static final Logger logger = LogManager.getLogger(User.class);

    protected LocalDateTime localDateTime;
    private long rethought;
    private static int nextId = 79127;
    protected int id;
    private String tAddress = "./THOUGHTS/";


    public Thought(String type, User user, User doed, String text, LocalDateTime localDateTime) {
        this.type = type;
        this.localDateTime = localDateTime;
        this.user = user.getId();
        if (type.equalsIgnoreCase("t")) {
            this.doed = 0;
        } else this.doed = doed.getId();
        this.text = text;
        this.id = nextId + Objects.requireNonNull(new File(tAddress).listFiles()).length;

        likes = 0;
        spamReports = 0;
        rethought = 0;
        opinions = new ArrayList<>();
        likers = new ArrayList<>();
        rethoughters = new ArrayList<>();


        Gson gson = new Gson();
        String json = gson.toJson(this);


        File ThoughtFile2 = new File("./THOUGHTS/" + id + ".txt");
        try {
            ThoughtFile2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try (PrintStream ps = new PrintStream(ThoughtFile2)) {
            ps.print(json);
            ps.close();
            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public Thought() {
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public ArrayList<Integer> getLikers() {
        return likers;
    }

    public ArrayList<Integer> getOpinions() {
        return opinions;
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public long getRethought() {
        return rethought;
    }

    public int getUser() {
        return user;
    }

    public ArrayList<Integer> getRethoughters() {
        return rethoughters;
    }

    public int getDoed() {
        return doed;
    }

    public String getType() {
        return type;
    }

    public void addRethought() {
        rethought++;
    }

    public void minusRethought() {
        if (rethought != 0) {
            rethought--;
        }
    }

    public void addLike() {
        likes++;
    }

    public void minusLike() {
        if (likes != 0) {
            likes--;
        }
    }

    public void addSpam() {
        spamReports++;
    }


    public void viewAsT() {
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "At " + localDateTime + "\n" + findUser(user).getUserName() + " Said: " + "\n" +
                text + "\n" + "------------------------------------------------" +
                "\n" + "Likes" + " :" + likes + "  " + "Opinions" + " :" + opinions.size() + "  " + "Rethoughts" + " :" + rethought + "\n" +
                "------------------------------------------------" + "\n" + "\n" + ConsoleColors.RESET);
    }

    public void viewAsO() {
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "At " + localDateTime + "\n" + findUser(user).getUserName() + " Replied To " + findUser(doed).getUserName() + ":" + "\n" +
                text + "\n" + "------------------------------------------------" +
                "\n" + "Likes" + " :" + likes + "  " + "Opinions" + " :" + opinions.size() + "  " + "Rethoughts" + " :" + rethought + "\n" +
                "------------------------------------------------" + "\n" + "\n" + ConsoleColors.RESET);
    }

    public void viewAsC() {
        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "At " + localDateTime + "\n" + findUser(user).getUserName() + " Said: " + "\n" +
                text + "\n" + "------------------------------------------------" +
                "\n" + "\n" + ConsoleColors.RESET);
    }


    public void view() {
        if (type.equalsIgnoreCase("t")) {
            viewAsT();
        }
        if (type.equalsIgnoreCase("o")) {
            viewAsO();
        }
        if (type.equalsIgnoreCase("c")) {
            viewAsC();
        }
    }


    public Thought loadThought() {

        File ThoughtFile = new File("./THOUGHTS/" + id + ".txt");

        String str = "";
        try {
            str = FileUtils.readFileToString(ThoughtFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Thought thought = gson.fromJson(str, Thought.class);

        logger.info("thought" + id + "loaded from a json file");
        return thought;

    }

    public void saveThought() {

        File ThoughtFile = new File("./THOUGHTS/" + id + ".txt");


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);

        try (PrintStream ps = new PrintStream(ThoughtFile)) {
            ps.print(json);
            ps.close();
            ps.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        logger.info("thought" + id + "saved as a json file");
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
                logger.info("user " + fUser.getId() + " file founded and opened ");
                return fUser;
            }

        }
        return null;
    }

}
