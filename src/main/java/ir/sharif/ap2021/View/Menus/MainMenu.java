package ir.sharif.ap2021.View.Menus;

import java.util.HashMap;

public class MainMenu extends Menu {
    public MainMenu() {
        super("Main Menu", null);
        HashMap<Integer, Menu> submenus = new HashMap<>();
        submenus.put(1, new PersonalMenu(this));
        submenus.put(2, new TimeLineMenu(this));
        submenus.put(3, new ExploreMenu(this));
        submenus.put(4, new ChatBoxMenu(this));
        submenus.put(5, new SettingMenu(this));
        this.setSubmenus(submenus);
    }
}