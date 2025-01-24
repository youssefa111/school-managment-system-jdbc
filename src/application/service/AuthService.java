package application.service;

public interface AuthService<T> {

    void login(String username, String password);
    void register(T entity);

}
