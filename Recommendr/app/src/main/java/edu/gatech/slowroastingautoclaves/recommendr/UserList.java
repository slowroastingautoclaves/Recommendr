package edu.gatech.slowroastingautoclaves.recommendr;

import java.util.ArrayList;

/**
 * Created by Diptodip on 2/15/16.
 */
public class UserList {
    private static UserList ourInstance = null;

    private ArrayList<User> users;

    private UserList() {
        users = new ArrayList<User>();
    }

    public static UserList getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserList();
        }
        return ourInstance;
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public void removeUser(User user) {
        this.users.remove(user);
    }
}
