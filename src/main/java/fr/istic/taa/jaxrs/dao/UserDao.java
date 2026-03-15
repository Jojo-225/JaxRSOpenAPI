package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.User;

public class UserDao extends AbstractJpaDao<Long,User>{
    public UserDao() {
        super();
        this.setClazz(User.class);
    }

    public User findByEmail(String email) {
        return this.entityManager
                .createQuery("SELECT u FROM User u WHERE u.mail = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    public User findByEmailAndPassword(String email, String password) {
        return this.entityManager
                .createQuery("SELECT u FROM User u WHERE u.mail = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

}
