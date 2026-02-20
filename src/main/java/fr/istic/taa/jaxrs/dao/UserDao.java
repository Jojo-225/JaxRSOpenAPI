package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.User;

public class UserDao extends AbstractJpaDao<Long,User>{
    public UserDao() {
        super();
        this.setClazz(User.class);
    }

}
