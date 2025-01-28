package utils;

import application.entity.User;
import utils.constants.Role;

public class CurrentSession {

    private CurrentSession(){}


    private static class CurrentSessionHolder{
        private static final CurrentSession INSTANCE = new CurrentSession();
    }

    public static CurrentSession getInstance(){
        return CurrentSessionHolder.INSTANCE;
    }

    private Role currentRole;
    private boolean isAuthenticated;
    private int userId;
    private User user;

    public Role getCurrentRole() {
        return currentRole;
    }
    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public int getUserId() {return userId;}
    public void setUserId(int userId) {this.userId = userId;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

}
