package fr.istic.taa.jaxrs.dao;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Ticket;
import java.util.List;

public class CustomerDao extends AbstractJpaDao<Long, Customer> {
    public CustomerDao() {
        super();
        this.setClazz(Customer.class);
    }

    public List<Ticket> findTicketsByCustomerId(Long customerId) {
        return this.entityManager
                .createQuery("SELECT t FROM Ticket t JOIN t.customers c WHERE c.id = :customerId", Ticket.class)
                .setParameter("customerId", customerId)
                .getResultList();
    }
}
