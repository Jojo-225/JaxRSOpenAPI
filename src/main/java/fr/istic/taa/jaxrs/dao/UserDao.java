package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.User;
import jakarta.persistence.NoResultException;

public class UserDao extends AbstractJpaDao<Long,User>{
    public UserDao() {
        super();
        this.setClazz(User.class);
    }

    public User findByEmail(String email) {
        try {
            return this.entityManager
                    .createQuery("SELECT u FROM User u WHERE u.mail = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User findByEmailAndPassword(String email, String password) {
        return this.entityManager
                .createQuery("SELECT u FROM User u WHERE u.mail = :email AND u.password = :password", User.class)
                .setParameter("email", email)
                .setParameter("password", password)
                .getSingleResult();
    }

}
