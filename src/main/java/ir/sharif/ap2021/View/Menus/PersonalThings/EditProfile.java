package ir.sharif.ap2021.View.Menus.PersonalThings;

import ir.sharif.ap2021.Controller.LogicalAgent;
import ir.sharif.ap2021.View.ConsoleColors;
import ir.sharif.ap2021.View.Menus.Menu;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;

public class EditProfile extends Menu {
    public EditProfile(Menu parentMenu) {

        super("Edit Profile", parentMenu);

        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, getFirstName());
        submenus.put(2, getLastName());
        submenus.put(3, getUserName());
        submenus.put(4, getBiography());
        submenus.put(5, getBirthday());
        submenus.put(6, getPhone());
        submenus.put(7, getEmail());
        submenus.put(8, getPassword());
        this.setSubmenus(submenus);
    }

    //log every action here
    protected Menu getFirstName() {
        return new Menu("Edit First Name", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your First Name :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);
                user.setFirstName(answer1);
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "FirstName Changed Successfully!" + ConsoleColors.RESET);
                logger.info("user " + user.getId() + " changed his/her firstname to " + answer1);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getLastName() {
        return new Menu("Edit Last Name", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Last Name :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);
                user.setLastName(answer1);
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "LastName Changed Successfully!" + ConsoleColors.RESET);
                logger.info("user " + user.getId() + " changed his/her lastname to " + answer1);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getUserName() {
        return new Menu("Edit User Name", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Username :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);
                while (new LogicalAgent().fileSearch(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "user already exists!" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " wanted to change username to a username which already exist");
                    answer1 = userAns(scanner);
                }


                File myUsername = new File("./USERS/" + user.getUserName() + ".txt");
                Path source = Paths.get("./USERS/" + user.getUserName() + ".txt");

                try {
                    Files.move(source, source.resolveSibling(answer1 + ".txt"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                user.setUserName(answer1);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "UserName Changed Successfully!" + ConsoleColors.RESET);

                logger.info("user " + user.getId() + " changed his/her username to " + answer1);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getBiography() {
        return new Menu("Edit Biography", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Biography :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);
                user.setBiography(answer1);
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Biography Changed Successfully!" + ConsoleColors.RESET);
                logger.info("user " + user.getId() + " changed his/her bigoraphy to " + answer1);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getBirthday() {
        return new Menu("Edit Birthday", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Day of Birth :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);

                while (!isValidDay(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " entered invalid day for birthday date");
                    answer1 = userAns(scanner);
                }
                String day = answer1;


                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Month of Birth :"
                        + ConsoleColors.RESET);
                answer1 = userAns(scanner);

                while (!isValidDay(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " entered invalid day for birthday date");
                    answer1 = userAns(scanner);
                }
                String month = answer1;

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Year of Birth :"
                        + ConsoleColors.RESET);
                answer1 = userAns(scanner);

                while (!isValidYear(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    answer1 = userAns(scanner);
                }
                String year = answer1;

                LocalDate birthday = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                user.setBirthday(birthday);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Birthday Changed Successfully!" + ConsoleColors.RESET);
                logger.info("user " + user.getId() + " changed his/her birthday to " + answer1);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getPhone() {
        return new Menu("Edit Phone Number", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Phone Number :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);

                while (!isValidPhone(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " entered invalid phone number");
                    answer1 = userAns(scanner);
                }
                user.setPhoneNumber(answer1);
                logger.info("user " + user.getId() + " changed his/her phone number to " + answer1);

                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Phone Number Changed Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getEmail() {
        return new Menu("Edit Email Address", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your Email Address :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);

                while (!isValidEmail(answer1)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "invalid answer!" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " entered invalid email address");
                    answer1 = userAns(scanner);
                }
                user.setEmail(answer1);
                logger.info("user " + user.getId() + " changed his/her email address to " + answer1);
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Email Address Changed Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }

    protected Menu getPassword() {
        return new Menu("Edit Password", this) {
            @Override
            public void show() {
                System.out.println(this.getName() + ":");
                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "Enter Your New Password :"
                        + ConsoleColors.RESET);
            }

            @Override
            public void execute() {

                String answer1 = userAns(scanner);
                String pass = answer1;

                System.out.println(ConsoleColors.YELLOW_BOLD_BRIGHT + "repeat your new password :"
                        + ConsoleColors.RESET);
                answer1 = userAns(scanner);

                while (!answer1.equals(pass)) {
                    System.out.println(ConsoleColors.RED_BOLD_BRIGHT + "incorrect repeat" + "\n" + "try again" + ConsoleColors.RESET);
                    logger.error("user " + user.getId() + " has failed in repeating password");
                    answer1 = userAns(scanner);
                }

                user.setPassword(answer1);
                logger.info("user " + user.getId() + " changed his/her password to " + answer1);
                System.out.println(ConsoleColors.BLUE_BOLD_BRIGHT + "Password Changed Successfully!" + ConsoleColors.RESET);

                user.saveUser();
                user = user.loadUser();
                this.parentMenu.show();
                this.parentMenu.execute();
            }
        };
    }


    public boolean isValidDay(String s) {

        if (s.length() != 2) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidYear(String s) {

        if (s.length() != 4) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidPhone(String s) {

        if (s.length() != 11) {
            return false;
        }

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidEmail(String s) {
        return s.length() > 6 && s.contains("@") && s.endsWith(".com");
    }
}







