package ir.sharif.ap2021.Controller;

import com.google.gson.Gson;

import ir.sharif.ap2021.View.Menus.MainMenu;
import ir.sharif.ap2021.View.Menus.Menu;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.Model.UserThings.User;
import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;


public class LogicalAgent {


    public static File userDirectory = new File("./USERS/");
    public static File thoughtDirectory = new File("./THOUGHTS/");





    public LogicalAgent() {
        if (!userDirectory.exists()) userDirectory.mkdirs();
        if (!thoughtDirectory.exists()) thoughtDirectory.mkdirs();
    }


    public void run() {

        System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Welcome to our gathering! Have we ever seen you?" + "\n" + "1.Yes" + "\n" + "2.No" + "\n" + "you can always quit by typing 'exit'" + ConsoleColors.RESET);
        Scanner scanner = new Scanner(System.in);


        String answer;
        answer = userAns(scanner);


        while (!isValidOption2(answer)) {
            System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
            answer = userAns(scanner);
        }


        if (answer.equals("1")) {
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Cool! May i know your username?" + ConsoleColors.RESET);
            answer = userAns(scanner);

            while (!fileSearch(answer)) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry we do not recognize your username" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);
            }

            String username = answer;

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "and password?" + ConsoleColors.RESET);
            answer = userAns(scanner);


            String str = "";
            try {
                str = FileUtils.readFileToString(new File("./USERS/" + username + ".txt"));

            } catch (IOException e) {
                e.printStackTrace();
            }

            Gson gson = new Gson();
            User user = gson.fromJson(str, User.class);
//            user = user.loadUser();


            while (!answer.equals(user.getPassword())) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Wrong Password!" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);
            }

            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Logged in Successfully" + ConsoleColors.RESET);




            Menu.setScanner(scanner);
            Menu.setUser(user);
            Menu currentMenu = new MainMenu();
            currentMenu.show();
            currentMenu.execute();

        } else if (answer.equals("2")) {

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "So we want to introduce you to the others in the party." + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "May I have your first name?" + ConsoleColors.RESET);
            answer = userAns(scanner);
            String firstname = answer;

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "May I have your last name?" + ConsoleColors.RESET);
            answer = userAns(scanner);
            String lastname = answer;

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "What should we call you (username)?" + ConsoleColors.RESET);
            answer = userAns(scanner);

            while (answer.equalsIgnoreCase("back")) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "Sorry This username cannot be Created!" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);

            }

            while (fileSearch(answer)) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "user already exists!" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);
            }

            String username = answer;

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Password?" + ConsoleColors.RESET);
            answer = userAns(scanner);
            String password = answer;

            System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Email?" + ConsoleColors.RESET);
            answer = userAns(scanner);

            while (!isValidEmail(answer) || emailSearch(answer)) {
                System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid email address or email already exists!" + "\n" + "try again" + ConsoleColors.RESET);
                answer = userAns(scanner);
            }

            String email = answer;
            User user = new User(firstname, lastname, username, email, password);
            System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Account Created Successfully!" + ConsoleColors.RESET);


            Menu.setScanner(scanner);
            Menu.setUser(user);
            Menu currentMenu = new MainMenu();
            currentMenu.show();
            currentMenu.execute();
        }


    }

    public static String userAns(Scanner scanner) {

        String s = scanner.nextLine();
        if (s.equals("exit")) {
            System.out.println("bye!");
            System.exit(0);
        }
        return s;
    }

    public boolean isValidOption2(String s) {
        if (s.equals("1")) {
            return true;
        }
        if (s.equals("2")) {
            return true;
        }

        return false;
    }

    public boolean isValidEmail(String s) {
        return s.length() > 6 && s.contains("@") && s.endsWith(".com");
    }

    public boolean fileSearch(String s) {

        int flag = 0;
        for (File f : userDirectory.listFiles()) {
            if (f.getName().equals(s + ".txt")) {
                flag = 1;
                break;
            }
        }
        return flag == 1;
    }

    public static boolean emailSearch(String s) {


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

            if (fUser.getEmail().equals(s)) {
                return true;
            }

        }
        return false;
    }

}





