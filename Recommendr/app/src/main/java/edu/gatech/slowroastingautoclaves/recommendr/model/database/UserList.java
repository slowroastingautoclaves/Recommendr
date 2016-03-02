package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;

/**
 * A singleton list of users used to store user data locally so that all activities have access.
 */
public class UserList {
    //Has instance of itself (singleton) and ArrayList<User> of User objects.
    private static UserList ourInstance = null;

    private ArrayList<User> users;

    private UserList() {
        users = new ArrayList<>();
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

    /**
     * Adds user to this list of users.
     * @param u is user to be added.
     */
    public void addUser(User u) {
        this.users.add(u);
    }

    /**
     * Removes user from this list of users.
     * @param u is user to be removed.
     */
    public void removeUser(User u) {
        this.users.remove(u);
    }

    /**
     * Finds user by email address from this list.
     * @param email is email address used to find user.
     * @return the user with matching email address.
     */
    public User findUserByEmail(String email) {
        for (User u : this.users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }
}