package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Customer;

public class CustomerDao extends AbstractJpaDao<Long, Customer> {
    public CustomerDao() {
        super();
        this.setClazz(Customer.class);
    }

}
