package utils;

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
}
