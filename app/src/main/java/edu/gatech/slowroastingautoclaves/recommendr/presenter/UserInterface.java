package edu.gatech.slowroastingautoclaves.recommendr.presenter;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;

/**
 * Created by daernwyn on 3/28/16.
 */
public interface UserInterface {
    User getUser(String identifier);
    void addUser(User u);
    void removeUser(String identifier);
    void lock(String identifier);
    void ban(String identifier);
    void unlock(String identifier);
    void unban(String identifier);
}