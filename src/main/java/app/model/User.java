package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private Boolean following = false;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, Boolean following) {
        this.name = name;
        this.following = following;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean isFollowing(){
        return following;
    }

    public void setFollowing(Boolean following){
        this.following = following;
    }
}