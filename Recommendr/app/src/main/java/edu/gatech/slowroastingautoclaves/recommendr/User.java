package edu.gatech.slowroastingautoclaves.recommendr;

/**
 * Created by Diptodip on 2/15/16.
 */
public class User {
    private String name, email, password, major;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return this.name;
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
