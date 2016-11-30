package edu.csumb.cst438.router;

/**
 * Created by pico on 11/20/16.
 */

public class User {

    public String username;
    public String bio;
    public String email;
    public String id;
    public String privacy;

    public User() {

    }

    public User(String username, String bio, String email, String id, String privacy) {
        this.username = username;
        this.bio = bio;
        this.email = email;
        this.id = id;
        this.privacy = privacy;
    }
}
