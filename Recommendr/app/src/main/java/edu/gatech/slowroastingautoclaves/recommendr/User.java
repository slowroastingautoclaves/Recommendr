package edu.gatech.slowroastingautoclaves.recommendr;

/**
 * Created by Diptodip on 2/15/16.
 */
public class User {
    private String username, email, password, major;

    public User(String name, String email, String password) {
        this.username = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMajor() {
        return this.major;
    }

    @Override
    public boolean equals(Object o) {
        User compare = (User) o;
        if (compare.getEmail().equals(this.getEmail())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getEmail().hashCode();
    }
}
