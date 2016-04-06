package edu.gatech.slowroastingautoclaves.recommendr.model.database;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import edu.gatech.slowroastingautoclaves.recommendr.model.User;

/**
 * A singleton list of users used to store user data locally so that all activities have access.
 */
public final class UserList {
    public static final String USERS = "users.bin";
    //Has instance of itself (singleton) and ArrayList<User> of User objects.
    private static UserList ourInstance = null;

    private ArrayList<User> users;

    private UserList() {
        users = null;
    }

    /**
     * Gets singleton instance, making a new one if necessary.
     * @return the instance of UserList
     */
    public static UserList getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserList();
            // make dummy users
            //ourInstance.addUser(new User("Foo", "foo@example.com", "hello"));
            //ourInstance.addUser(new User("admin", "user@admin.com", "12345", Condition.UNLOCKED, true));
            //ourInstance.addUser(new User("bad", "user@ban.com","12345", Condition.BANNED, false));
        }
        return ourInstance;
    }

    public ArrayList<User> getUsers() {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        return this.users;
    }

    /**
     * Adds user to this list of users.
     * @param u is user to be added.
     */
    public void addUser(User u) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
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
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        for (User u : this.users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public boolean loadUsers(File users) {
        boolean success = true;
        try {
            /*
              To read, we must use the ObjectInputStream since we want to read our model in with
              a single read.
             */
            // assuming we saved our top level object, we read it back in with one line of code.
            if (this.users == null) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(users));
                //noinspection unchecked
                this.users = (ArrayList<User>) in.readObject();
                Log.i("UserList", "Users loaded.");
                in.close();
            }
        } catch (IOException e) {
            Log.e("UserList", "Error reading an entry from binary file");
            success = false;
        } catch (ClassNotFoundException e) {
            Log.e("UserList", "Error casting a class from the binary file");
            success = false;
        }

        return success;
    }

    public boolean saveUsers(File users) {
        boolean success = true;
        try {
            /*
               For binary, we use Serialization, so everything we write has to implement
               the Serializable interface.  Fortunately all the collection classes and APi classes
               that we might use are already Serializable.  You just have to make sure your
               classes implement Serializable.

               We have to use an ObjectOutputStream to write objects.

               One thing to be careful of:  You cannot serialize static data.
             */
            ObjectOutputStream usersOut = new ObjectOutputStream(new FileOutputStream(users));
            // We basically can save our entire data model with one write, since this will follow
            // all the links and pointers to save everything.  Just save the top level object.
            usersOut.writeObject(this.users);
            usersOut.close();
        } catch (IOException e) {
            Log.e("UserList", "Error writing an entry from binary file");
            success = false;
        }
        return success;
    }
}
