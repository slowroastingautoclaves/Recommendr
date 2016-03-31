package edu.gatech.slowroastingautoclaves.recommendr.presenter;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;

/**
 * Created by daernwyn on 3/28/16.
 */
public interface UserInterface {
    /**
     * Gets user given identifier.
     * @param identifier is email of user to retrieve.
     * @return the corresponding User object
     */
    User getUser(String identifier);

    /**
     * adds a user to the list of users
     * @param u is User to add
     */
    void addUser(User u);

    /**
     * removes a user from the list of users
     * @param identifier is email of user to remove (or username)
     */
    void removeUser(String identifier);

    /**
     * locks a user
     * @param identifier is email of user to lock (or username)
     */
    void lock(String identifier);

    /**
     * bans a user
     * @param identifier see above
     */
    void ban(String identifier);

    /**
     * unlocks a user
     * @param identifier see above
     */
    void unlock(String identifier);

    /**
     * unbans a user
     * @param identifier see above
     */
    void unban(String identifier);
}