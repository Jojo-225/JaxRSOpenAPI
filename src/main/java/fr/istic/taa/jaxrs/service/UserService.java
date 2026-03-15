package fr.istic.taa.jaxrs.service;
import fr.istic.taa.jaxrs.dao.UserDao;
import fr.istic.taa.jaxrs.domain.User;

public class UserService {

    private final UserDao userDao = new UserDao();

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User findByEmailAndPassword(String email, String password) {
        return userDao.findByEmailAndPassword(email, password);
    }

    public User findOne(Long id) {
        return userDao.findOne(id);
    }

    public java.util.List<User> findAll() {
        return userDao.findAll();
    }

    // public void updateEmail(String newEmail, User user) {
    //     if (userDao.findByEmail(newEmail) != null) {
    //         throw new RuntimeException("A user with this email already exists");
    //     }
    //     user.setMail(newEmail);
    //     userDao.update(user);
    // }


}
