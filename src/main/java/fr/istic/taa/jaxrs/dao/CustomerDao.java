package fr.istic.taa.jaxrs.dao;

import java.util.List;
import java.util.ArrayList;

import fr.istic.taa.jaxrs.dao.generic.AbstractJpaDao;
import fr.istic.taa.jaxrs.domain.Customer;
import fr.istic.taa.jaxrs.domain.Organizer;
import fr.istic.taa.jaxrs.domain.Ticket;
import jakarta.persistence.EntityTransaction;

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

    public Customer findWithNotificationPreferences(Long customerId) {
        List<Customer> customers = this.entityManager
                .createQuery(
                        "SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.followedOrganizers WHERE c.id = :customerId",
                        Customer.class
                )
                .setParameter("customerId", customerId)
                .getResultList();

        return customers.isEmpty() ? null : customers.get(0);
    }

    public Customer updateNotificationPreferences(Long customerId, boolean notifyAllOrganizers, List<Long> organizerIds) {
        EntityTransaction transaction = this.entityManager.getTransaction();

        try {
            transaction.begin();

            Customer customer = this.entityManager.find(Customer.class, customerId);
            if (customer == null) {
                transaction.commit();
                return null;
            }

            customer.setNotifyAllOrganizers(notifyAllOrganizers);
            customer.setFollowedOrganizers(findOrganizersByIds(organizerIds));

            Customer updated = this.entityManager.merge(customer);
            transaction.commit();
            return updated;
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    private List<Organizer> findOrganizersByIds(List<Long> organizerIds) {
        if (organizerIds == null || organizerIds.isEmpty()) {
            return new ArrayList<>();
        }

        return this.entityManager
                .createQuery("SELECT o FROM Organizer o WHERE o.id IN :organizerIds", Organizer.class)
                .setParameter("organizerIds", organizerIds)
                .getResultList();
    }

    // TODO : Creitera pour trouver les clients qui ont acheté des tickets pour un concert spécifique 
}
